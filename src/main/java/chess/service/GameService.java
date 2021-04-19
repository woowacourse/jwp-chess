package chess.service;

import chess.controller.dto.BoardDto;
import chess.controller.dto.GameDto;
import chess.controller.dto.ScoresDto;
import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.domain.board.position.Horizontal;
import chess.domain.board.position.Position;
import chess.domain.board.position.Vertical;
import chess.domain.piece.Owner;
import chess.domain.piece.Piece;
import chess.domain.player.Turn;
import chess.service.dao.GameDao;
import chess.view.PieceSymbolMapper;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameService {

    private final GameDao gameDao;

    public GameService(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public void create(final Long roomId) {
        final ChessGame chessGame = ChessGame.initNew();
        gameDao.save(roomId, chessGame.turn(), chessGame.board());
    }

    public void delete(final Long roomId) {
        gameDao.delete(roomId);
    }

    public List<String> show(final Long roomId, final Position source) {
        final ChessGame chessGame = loadChessGame(roomId);
        return chessGame.reachablePositions(source);
    }

    public void move(final Long roomId, final Position source, final Position target) {
        final ChessGame chessGame = loadChessGame(roomId);
        chessGame.move(source, target);
        gameDao.update(roomId, chessGame.turn(), chessGame.board());
    }

    public boolean isGameEnd(final Long roomId) {
        return loadChessGame(roomId).isGameEnd();
    }

    public ChessGame loadChessGame(final Long roomId) {
        final GameDto gameDto = gameDao.load(roomId);
        String[] data = gameDto.getBoard().split(",");
        return ChessGame.load(dataToBoard(data), Turn.of(gameDto.getTurn()));
    }

    private Board dataToBoard(String[] data) {
        Map<Position, Piece> board = new HashMap<>(); 
        int index = 0;
        for (Vertical v : Vertical.values()) {
            for (Horizontal h : Horizontal.values()) {
                board.put(new Position(v, h), PieceSymbolMapper.parseToPiece(data[index++]));
            }
        }
        return new Board(board);
    }

    public ScoresDto scores(final Long roomId) {
        final ChessGame chessGame = loadChessGame(roomId);
        return new ScoresDto(chessGame.score(Owner.BLACK), chessGame.score(Owner.WHITE));
    }

    public BoardDto board(final Long roomId) throws SQLException {
        final ChessGame chessGame = loadChessGame(roomId);
        return new BoardDto(chessGame.boardDto());
    }

    public List<Owner> winner(final Long roomId) {
        return loadChessGame(roomId).winner();
    }
}
