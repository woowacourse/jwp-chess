package chess.service;

import chess.domain.chessboard.ChessBoard;
import chess.domain.command.GameCommand;
import chess.domain.game.ChessGame;
import chess.domain.piece.Color;
import chess.domain.piece.EmptyPiece;
import chess.domain.piece.Piece;
import chess.domain.piece.generator.NormalPiecesGenerator;
import chess.domain.position.Position;
import chess.domain.state.State;
import chess.dto.BoardDto;
import chess.dto.GameDto;
import chess.dto.PieceDto;
import chess.dto.StatusDto;
import chess.repository.BoardDao;
import chess.repository.GameDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChessService {

    private final GameDao gameDao;
    private final BoardDao boardDao;

    public ChessService(GameDao gameDao, BoardDao boardDao) {
        this.gameDao = gameDao;
        this.boardDao = boardDao;
    }

    @Transactional
    public int insertGame(String title, String password, ChessBoard chessBoard) {
        validateTitle(title);
        ChessGame chessGame = new ChessGame(chessBoard);
        chessGame.playGameByCommand(GameCommand.of("start"));

        int id = gameDao.save(title, password, chessGame.getState().toString());
        boardDao.save(chessGame.getChessBoard(), id);
        return id;
    }

    private void validateTitle(String title) {
        if (title.isBlank()) {
            throw new IllegalArgumentException("제목을 입력해주세요.");
        }
    }

    public List<GameDto> findGame() {
        return gameDao.findAll();
    }

    public BoardDto selectBoard(int id) {
        ChessBoard chessBoard = boardDao.findById(id);
        Map<Position, Piece> pieces = chessBoard.getPieces();
        Map<String, PieceDto> board = new HashMap<>();
        for (Position position : pieces.keySet()) {
            String key = position.toString();
            Piece piece = pieces.get(Position.of(key));
            PieceDto pieceDto = new PieceDto(piece.getSymbol().name(), piece.getColor().name());
            board.put(key, pieceDto);
        }
        return new BoardDto(board);
    }

    public String selectWinner(int gameId) {
        if (gameDao.findGameCount() == 0) {
            return null;
        }
        State state = gameDao.findState(gameId);
        ChessBoard chessBoard = boardDao.findById(gameId);

        ChessGame chessGame = new ChessGame(state, chessBoard);
        if (chessGame.isEndGameByPiece()) {
            return chessGame.getWinner().name();
        }
        return null;
    }

    public String selectState(int gameId) {
        return gameDao.findState(gameId).toString();
    }

    public StatusDto selectStatus(int gameId) {
        ChessGame chessGame = new ChessGame(gameDao.findState(gameId), boardDao.findById(gameId));
        Map<Color, Double> scores = chessGame.calculateScore();

        return new StatusDto(scores.get(Color.WHITE), scores.get(Color.BLACK));
    }

    @Transactional
    public void updateBoard(int gameId, String from, String to) {
        ChessGame chessGame = new ChessGame(gameDao.findState(gameId), boardDao.findById(gameId));
        chessGame.playGameByCommand(GameCommand.of("move", from, to));
        chessGame.isEndGameByPiece();
        gameDao.update(chessGame.getState().toString(), gameId);

        Map<String, Piece> pieces = chessGame.getChessBoard().toMap();
        boardDao.update(Position.of(to), pieces.get(to), gameId);
        boardDao.update(Position.of(from), EmptyPiece.getInstance(), gameId);
    }

    @Transactional
    public void endGame(int gameId) {
        ChessGame chessGame = new ChessGame(gameDao.findState(gameId), boardDao.findById(gameId));
        chessGame.playGameByCommand(GameCommand.of("end"));
        gameDao.update(chessGame.getState().toString(), gameId);
    }

    @Transactional
    public void deleteGame(int gameId, String password) {
        String value = gameDao.findPassword(gameId);
        State state = gameDao.findState(gameId);
        validateState(state);
        if (value.equals(password)) {
            gameDao.delete(gameId);
            return;
        }
        throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
    }

    private void validateState(State state) {
        if (!state.isFinished()) {
            throw new IllegalStateException("진행중인 게임은 삭제할 수 없습니다.");
        }
    }

    @Transactional
    public void restartGame(int gameId) {
        ChessBoard chessBoard = new ChessBoard(new NormalPiecesGenerator());
        int id = gameDao.update("WhiteRunning", gameId);
        boardDao.delete(gameId);
        boardDao.save(chessBoard, id);
    }
}
