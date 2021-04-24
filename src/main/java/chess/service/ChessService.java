package chess.service;

import chess.dao.GameDao;
import chess.domain.ChessGameManager;
import chess.domain.piece.Color;
import chess.domain.position.Position;
import chess.dto.*;
import org.springframework.stereotype.Service;

@Service
public class ChessService {
    private final GameDao gameDAO;

    public ChessService(GameDao gameDAO) {
        this.gameDAO = gameDAO;
    }

    public CommonDto<GameListDto> loadGameList() {
        return new CommonDto<>("게임 목록을 불러왔습니다.", new GameListDto(gameDAO.loadGameList()));
    }

    public CommonDto<NewGameDto> saveNewGame() {
        ChessGameManager chessGameManager = new ChessGameManager();
        chessGameManager.start();

        SavedGameDto savedGameDto = new SavedGameDto(
                ChessBoardDto.from(chessGameManager.getBoard()),
                chessGameManager.getCurrentTurnColor().name());
        int gameId = gameDAO.saveGame(savedGameDto);

        return new CommonDto<>("새로운 게임이 생성되었습니다.",
                NewGameDto.from(chessGameManager, gameId));
    }

    public CommonDto<RunningGameDto> loadGame(int gameId) {
        ChessGameManager chessGameManager = loadChessGameManager(gameId);
        return new CommonDto<>("게임을 불러왔습니다.", toRunningGameDto(chessGameManager));
    }

    public CommonDto<RunningGameDto> move(int gameId, String startPosition, String endPosition) {
        ChessGameManager chessGameManager = loadChessGameManager(gameId);
        chessGameManager.move(Position.of(startPosition), Position.of(endPosition));

        updateDatabase(chessGameManager, gameId);

        return new CommonDto<>("기물을 이동했습니다.",
                toRunningGameDto(chessGameManager));
    }

    private void updateDatabase(ChessGameManager chessGameManager, int gameId) {
        if (chessGameManager.isEnd()) {
            gameDAO.deletePiecesByGameId(gameId);
            gameDAO.deleteGameByGameId(gameId);
            return;
        }

        gameDAO.updateTurnByGameId(chessGameManager.getCurrentTurnColor(), gameId);
        gameDAO.updatePiecesByGameId(ChessBoardDto.from(chessGameManager.getBoard()), gameId);
    }

    private RunningGameDto toRunningGameDto(ChessGameManager chessGameManager) {
        return RunningGameDto.of(
                chessGameManager.getBoard(),
                chessGameManager.getCurrentTurnColor(),
                chessGameManager.isEnd());
    }

    private ChessGameManager loadChessGameManager(int gameId) {
        SavedGameDto savedGameDto = gameDAO.loadGame(gameId);

        ChessGameManager chessGameManager = new ChessGameManager();
        chessGameManager.load(
                savedGameDto.getChessBoardDto().toChessBoard(),
                Color.of(savedGameDto.getCurrentTurnColor()));
        return chessGameManager;
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
