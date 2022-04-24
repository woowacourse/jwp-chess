package chess.service;

import chess.dao.BoardDao;
import chess.dao.GameDao;
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
import chess.dto.PieceDto;
import chess.dto.StatusDto;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChessService {

    private final GameDao gameDao;
    private final BoardDao boardDao;

    @Autowired
    public ChessService(GameDao gameDao, BoardDao boardDao) {
        this.gameDao = gameDao;
        this.boardDao = boardDao;
    }

    public BoardDto selectBoard() {
        ChessBoard chessBoard = boardDao.find();
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

    public String selectWinner() {
        if (gameDao.findGameCount() == 0) {
            return null;
        }
        ChessBoard chessBoard = boardDao.find();
        State state = gameDao.findState();

        ChessGame chessGame = new ChessGame(state, chessBoard);
        if (chessGame.isEndGameByPiece()) {
            return chessGame.getWinner().name();
        }
        return null;
    }

    @Transactional
    public void insertGame() {
        ChessGame chessGame = new ChessGame(new NormalPiecesGenerator());
        chessGame.playGameByCommand(GameCommand.of("start"));

        gameDao.delete();
        gameDao.save(chessGame.getState().toString());
        boardDao.save(chessGame.getChessBoard(), gameDao.findId());
    }

    @Transactional
    public void updateBoard(String from, String to) {
        ChessGame chessGame = new ChessGame(gameDao.findState(), boardDao.find());
        chessGame.playGameByCommand(GameCommand.of("move", from, to));
        chessGame.isEndGameByPiece();
        gameDao.update(chessGame.getState().toString(), gameDao.findId());

        Map<String, Piece> pieces = chessGame.getChessBoard().toMap();
        boardDao.update(Position.of(to), pieces.get(to), gameDao.findId());
        boardDao.update(Position.of(from), EmptyPiece.getInstance(), gameDao.findId());
    }

    public StatusDto selectStatus() {
        ChessGame chessGame = new ChessGame(gameDao.findState(), boardDao.find());
        chessGame.playGameByCommand(GameCommand.of("status"));
        Map<Color, Double> scores = chessGame.calculateScore();

        return new StatusDto(scores.get(Color.WHITE), scores.get(Color.BLACK));
    }

    @Transactional
    public void deleteGame() {
        ChessGame chessGame = new ChessGame(gameDao.findState(), boardDao.find());

        chessGame.playGameByCommand(GameCommand.of("end"));
        gameDao.delete();
    }
}
