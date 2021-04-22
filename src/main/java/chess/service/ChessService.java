package chess.service;

import chess.dao.GameDao;
import chess.domain.board.BoardFactory;
import chess.domain.board.ChessBoard;
import chess.domain.order.MoveResult;
import chess.domain.piece.Color;
import chess.domain.piece.ColoredPieces;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.domain.statistics.ChessGameStatistics;
import chess.domain.statistics.MatchResult;
import chess.dto.*;
import chess.exception.DomainException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class ChessService {
    private final GameDao gameDAO;

    public ChessService(GameDao gameDAO) {
        this.gameDAO = gameDAO;
    }

    public CommonDto<NewGameDto> saveNewGame() {
        ChessBoard chessBoard = BoardFactory.createBoard();
        Color currentTurnColor = Color.WHITE;
        Map<Color, Double> scoreMap = chessBoard.getScoreMap();
        ChessGameStatistics chessGameStatistics = new ChessGameStatistics(scoreMap,
                MatchResult.generateMatchResult(scoreMap.get(Color.WHITE), scoreMap.get(Color.BLACK)));
        Map<String, PieceDto> board = ChessBoardDto.from(chessBoard).board();
        int gameId = gameDAO.saveGame(currentTurnColor, board);
        return new CommonDto<>("새로운 게임이 생성되었습니다.",
                new NewGameDto(gameId, board, currentTurnColor, chessGameStatistics.getColorsScore()));
    }

    public CommonDto<RunningGameDto> move(int gameId, String startPosition, String endPosition) {
        RunningGameDto runningGameDto = gameDAO.loadGame(gameId);
        ChessBoard chessBoard = toChessBoard(runningGameDto.getChessBoard());
        Position from = Position.of(startPosition);
        validateProperPieceAtFromPosition(from, chessBoard, runningGameDto.getCurrentTurnColor());
        Position to = Position.of(endPosition);
        MoveResult moveResult = chessBoard.move(chessBoard.createMoveRoute(from, to));
        Color currentTurnColor = runningGameDto.getCurrentTurnColor();
        List<ColoredPieces> coloredPieces = loadColoredPieces(chessBoard);
        if (moveResult.isCaptured()) {
            ColoredPieces opposite = findByColor(currentTurnColor.opposite(), coloredPieces);
            opposite.remove(moveResult.getCapturedPiece());
        }
        currentTurnColor = currentTurnColor.opposite();
        boolean kingDead = isKingDead(coloredPieces);
        updateDatabase(chessBoard, currentTurnColor, kingDead, gameId);
        return new CommonDto<>("기물을 이동했습니다.", RunningGameDto.of(chessBoard, currentTurnColor, kingDead));
    }

    private void updateDatabase(ChessBoard chessBoard, Color currentTurnColor, boolean kingDead, int gameId) {
        if (kingDead) {
            gameDAO.deletePiecesByGameId(gameId);
            gameDAO.deleteGameByGameId(gameId);
            return;
        }
        gameDAO.updateTurnByGameId(currentTurnColor, gameId);
        gameDAO.updatePiecesByGameId(ChessBoardDto.from(chessBoard), gameId);
    }

    private ChessBoard toChessBoard(Map<String, PieceDto> chessBoard) {
        ChessBoardDto chessBoardDto = new ChessBoardDto(chessBoard);
        return chessBoardDto.toChessBoard();
    }

    private List<ColoredPieces> loadColoredPieces(ChessBoard chessBoard) {
        return chessBoard.board().values().stream()
                .filter(Piece::isNotBlank)
                .collect(groupingBy(Piece::getColor))
                .entrySet()
                .stream()
                .map(entry -> new ColoredPieces(entry.getValue(), entry.getKey()))
                .collect(toList());
    }

    private boolean isKingDead(List<ColoredPieces> coloredPieces) {
        return coloredPieces.stream().anyMatch(ColoredPieces::isKingDead);
    }

    private void validateProperPieceAtFromPosition(Position from, ChessBoard chessBoard, Color currentTurnColor) {
        validateHasPieceAtFromPosition(from, chessBoard);
        validateTurn(from, chessBoard, currentTurnColor);
    }

    private void validateHasPieceAtFromPosition(Position from, ChessBoard chessBoard) {
        if (!chessBoard.hasPiece(from)) {
            throw new DomainException("해당 위치에는 말이 없습니다.");
        }
    }

    private void validateTurn(Position from, ChessBoard chessBoard, Color currentTurnColor) {
        if (!chessBoard.getPieceByPosition(from).isSameColor(currentTurnColor)) {
            throw new DomainException("현재 움직일 수 있는 진영의 기물이 아닙니다.");
        }
    }

    private ColoredPieces findByColor(Color color, List<ColoredPieces> coloredPieces) {
        return coloredPieces.stream()
                .filter(pieces -> pieces.isSameColor(color))
                .findAny()
                .orElseThrow(() -> new DomainException("시스템 에러 - 진영을 찾을 수 없습니다."));
    }

    public CommonDto<GameListDto> loadGameList() {
        return new CommonDto<>("게임 목록을 불러왔습니다.", new GameListDto(gameDAO.loadGameList()));
    }

    public CommonDto<RunningGameDto> loadGame(int gameId) {
        RunningGameDto runningGameDto = gameDAO.loadGame(gameId);
        ChessBoard chessBoard = toChessBoard(runningGameDto.getChessBoard());
        Color currentTurnColor = runningGameDto.getCurrentTurnColor();
        boolean isKingDead = isKingDead(loadColoredPieces(chessBoard));
        return new CommonDto<>("게임을 불러왔습니다", RunningGameDto.of(chessBoard, currentTurnColor, isKingDead));
    }
}
