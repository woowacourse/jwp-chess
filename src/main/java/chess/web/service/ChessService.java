package chess.web.service;

import chess.domain.Board;
import chess.domain.ChessBoard;
import chess.domain.ChessGame;
import chess.domain.Color;
import chess.domain.generator.InitBoardGenerator;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.domain.state.StateType;
import chess.web.dao.GameDao;
import chess.web.dao.PieceDao;
import chess.web.dto.board.BoardDto;
import chess.web.dto.board.MoveRequestDto;
import chess.web.dto.board.MoveResponseDto;
import chess.web.dto.board.PieceDto;
import chess.web.dto.board.PiecesDto;
import chess.web.dto.board.ResultDto;
import chess.web.dto.game.GameRequestDto;
import chess.web.dto.game.GameResponseDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private static final int BOARD_START_INDEX = 0;
    private static final int BOARD_END_INDEX = 7;

    private final GameDao gameDao;
    private final PieceDao pieceDao;

    public ChessService(GameDao gameDao, PieceDao pieceDao) {
        this.gameDao = gameDao;
        this.pieceDao = pieceDao;
    }

    public int newGame(String title, String password) {
        int gameId = gameDao.save(title, password, StateType.READY);
        start(gameId);
        return gameId;
    }

    public void restart(int gameId) {
        pieceDao.deleteAllByGameId(gameId);
        start(gameId);
    }

    private void start(int gameId) {
        gameDao.updateStateById(gameId, StateType.WHITE_TURN);
        ChessBoard chessBoard = new ChessBoard(new InitBoardGenerator());
        initChessBoard(chessBoard.getBoard(), gameId);
    }

    private void initChessBoard(Board board, int gameId) {
        for (int rankIndex = BOARD_START_INDEX; rankIndex <= BOARD_END_INDEX; rankIndex++) {
            initOneRank(board, rankIndex, gameId);
        }
    }

    private void initOneRank(Board board, int rankIndex, int gameId) {
        for (int fileIndex = BOARD_START_INDEX; fileIndex <= BOARD_END_INDEX; fileIndex++) {
            Position position = new Position(fileIndex, rankIndex);
            Piece piece = board.findPiece(position);
            pieceDao.save(gameId, new PieceDto(piece, position));
        }
    }

    public List<GameResponseDto> getAllGame() {
        return gameDao.findAll();
    }

    public void deleteGame(GameRequestDto gameRequestDto) {
        int gameId = gameRequestDto.getId();
        String password = gameRequestDto.getPassword();
        String realPassword = gameDao.findPasswordById(gameId);

        validateDeleteGame(gameId, password, realPassword);

        gameDao.deleteGameById(gameId);
    }

    private void validateDeleteGame(int gameId, String password, String realPassword) {
        if (!realPassword.equals(password)) {
            throw new IllegalArgumentException("비밀번호가 잘못되었습니다.");
        }
        if (getChessGame(gameId).isRunning()) {
            throw new IllegalArgumentException("게임이 아직 진행중입니다.");
        }
    }

    private ChessGame getChessGame(int gameId) {
        PiecesDto piecesDto = new PiecesDto(getPieces(gameId));
        ChessBoard chessBoard = new ChessBoard(piecesDto.toBoard());
        return new ChessGame(getCurrentChessSate(gameId).newState(chessBoard));
    }

    private List<PieceDto> getPieces(int gameId) {
        return pieceDao.findAllByGameId(gameId);
    }

    public StateType getCurrentChessSate(int gameId) {
        return gameDao.findStateById(gameId);
    }

    public BoardDto getBoard(int gameId) {
        return new BoardDto(gameId, new PiecesDto(getPieces(gameId)), getScore(gameId, Color.BLACK),
                getScore(gameId, Color.WHITE));
    }

    private double getScore(int gameId, Color color) {
        return getChessGame(gameId).score(color);
    }

    public MoveResponseDto getMoveResult(int gameId, MoveRequestDto moveRequestDto) {
        ChessGame chessGame = getChessGame(gameId);

        chessGame.move(moveRequestDto.getSource(), moveRequestDto.getTarget());
        Position sourcePosition = new Position(moveRequestDto.getSource());
        Position targetPosition = new Position(moveRequestDto.getTarget());
        move(gameId, chessGame, sourcePosition, targetPosition);

        return new MoveResponseDto(chessGame.isFinished());
    }

    private void move(int gameId, ChessGame chessGame, Position target, Position source) {
        gameDao.updateStateById(gameId, chessGame.getStateType());
        pieceDao.updateByGameId(gameId, new PieceDto(chessGame.board().findPiece(target), target));
        pieceDao.updateByGameId(gameId, new PieceDto(chessGame.board().findPiece(source), source));
    }

    public ResultDto getChessResult(int gameId) {
        ChessGame chessGame = getChessGame(gameId);
        endGame(chessGame, gameId);

        double blackScore = getScore(gameId, Color.BLACK);
        double whiteScore = getScore(gameId, Color.WHITE);


        return new ResultDto(gameId, blackScore, whiteScore, chessGame.result());
    }

    private void endGame(ChessGame chessGame, int gameId) {
        if (!chessGame.isFinished()) {
            chessGame.end();
            gameDao.updateStateById(gameId, StateType.END);
        }
    }
}
