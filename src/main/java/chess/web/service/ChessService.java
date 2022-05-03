package chess.web.service;

import chess.domain.Board;
import chess.domain.ChessBoard;
import chess.domain.ChessGame;
import chess.domain.Color;
import chess.domain.generator.InitBoardGenerator;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.domain.state.StateType;
import chess.exception.ChessGameException;
import chess.web.dao.GameDao;
import chess.web.dao.PieceDao;
import chess.web.dto.board.BoardDto;
import chess.web.dto.board.MovePositionsDto;
import chess.web.dto.board.MoveResultDto;
import chess.web.dto.board.PieceDto;
import chess.web.dto.board.PiecesDto;
import chess.web.dto.board.ResultDto;
import chess.web.dto.game.PasswordDto;
import chess.web.dto.game.TitleDto;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private static final Set<StateType> RUNNING_STATE_TYPES = Set.of(StateType.WHITE_TURN, StateType.BLACK_TURN);
    private static final Set<StateType> FINISHED_STATE_TYPES =
            Set.of(StateType.END, StateType.WHITE_WIN, StateType.BLACK_WIN);

    private static final int BOARD_START_INDEX = 0;
    private static final int BOARD_END_INDEX = 7;

    private final GameDao gameDao;
    private final PieceDao pieceDao;

    public ChessService(GameDao gameDao, PieceDao pieceDao) {
        this.gameDao = gameDao;
        this.pieceDao = pieceDao;
    }

    public int newGame(String title, String password) {
        int gameId = gameDao.save(title, password, StateType.WHITE_TURN);
        start(gameId);
        return gameId;
    }

    public void restart(int gameId) {
        pieceDao.deleteAllByGameId(gameId);
        gameDao.updateStateById(gameId, StateType.WHITE_TURN);
        start(gameId);
    }

    private void start(int gameId) {
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

    public List<TitleDto> getAllGame() {
        return gameDao.findAll();
    }

    public void deleteGame(PasswordDto passwordDto) {
        int gameId = passwordDto.getId();
        String password = passwordDto.getPassword();
        String realPassword = gameDao.findPasswordById(gameId);

        validateDeleteGame(gameId, password, realPassword);

        gameDao.deleteGameById(gameId);
    }

    private void validateDeleteGame(int gameId, String password, String realPassword) {
        if (!realPassword.equals(password)) {
            throw new ChessGameException("비밀번호가 잘못되었습니다.");
        }
        if (isChessRunning(gameId)) {
            throw new ChessGameException("게임이 아직 진행중입니다.");
        }
    }

    private boolean isChessRunning(int gameId) {
        return RUNNING_STATE_TYPES.contains(getStateType(gameId));
    }

    public StateType getStateType(int gameId) {
        return gameDao.findStateById(gameId);
    }

    public BoardDto getBoard(int gameId) {
        return new BoardDto(gameId, new PiecesDto(getPieces(gameId)), getScore(gameId, Color.BLACK),
                getScore(gameId, Color.WHITE));
    }

    private List<PieceDto> getPieces(int gameId) {
        return pieceDao.findAllByGameId(gameId);
    }

    private double getScore(int gameId, Color color) {
        return getChessGame(gameId).score(color);
    }

    private ChessGame getChessGame(int gameId) {
        PiecesDto piecesDto = new PiecesDto(getPieces(gameId));
        ChessBoard chessBoard = new ChessBoard(piecesDto.toBoard());
        return new ChessGame(getStateType(gameId).newState(chessBoard));
    }

    public MoveResultDto getMoveResult(int gameId, MovePositionsDto movePositionsDto) {
        ChessGame chessGame = getChessGame(gameId);

        chessGame.move(movePositionsDto.getSource(), movePositionsDto.getTarget());
        move(gameId, chessGame, movePositionsDto.getSource(), movePositionsDto.getTarget());

        return new MoveResultDto(chessGame.isFinished());
    }

    private void move(int gameId, ChessGame chessGame, Position target, Position source) {
        gameDao.updateStateById(gameId, chessGame.getStateType());
        pieceDao.updateByGameId(gameId, new PieceDto(chessGame.board().findPiece(target), target));
        pieceDao.updateByGameId(gameId, new PieceDto(chessGame.board().findPiece(source), source));
    }

    public boolean isChessFinished(int gameId) {
        return FINISHED_STATE_TYPES.contains(getStateType(gameId));
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
