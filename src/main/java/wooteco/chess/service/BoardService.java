package wooteco.chess.service;

import wooteco.chess.ChessGame;
import wooteco.chess.dao.BoardDao;
import wooteco.chess.dao.PlayerDao;
import wooteco.chess.dao.RoomDao;
import wooteco.chess.domain.Board;
import wooteco.chess.domain.Turn;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.state.BoardRepository;
import wooteco.chess.domain.state.Playing;
import wooteco.chess.dto.GameDto;

import java.sql.SQLException;

public class BoardService {
    private final BoardDao boardDao = new BoardDao();
    private final RoomDao roomDao = new RoomDao();
    private final PlayerDao playerDao = new PlayerDao();

    public Board create(int roomId) throws SQLException, ClassNotFoundException {
        Board board = boardDao.create(roomId, BoardRepository.create());
        return board;
    }

    public GameDto load(int roomId) throws SQLException, ClassNotFoundException {
        Board board = boardDao.findByRoomId(roomId);
        int turnPlayerId = roomDao.findTurnPlayerId(roomId);
        Turn turn = playerDao.findTurn(turnPlayerId);
        ChessGame game = new ChessGame(new Playing(board, turn));
        return new GameDto(board.getBoard(), turn, game.status());
    }

    public GameDto move(int roomId, Position source, Position target) throws SQLException, ClassNotFoundException {
            Board board = boardDao.findByRoomId(roomId);
            int turnPlayerId = roomDao.findTurnPlayerId(roomId);
            Turn turn = playerDao.findTurn(turnPlayerId);

            ChessGame game = new ChessGame(new Playing(board, turn));
            game.move(source, target);

            Piece sourcePiece = board.getBoard().get(source);
            Piece targetPiece = board.getBoard().get(target);
            boardDao.updateBoard(roomId, source, sourcePiece);
            boardDao.updateBoard(roomId, target, targetPiece);

            roomDao.updateTurn(roomId);
            return new GameDto(game.board().getBoard(), turn, game.status());
    }

    public int createRoom(int player1Id, int player2Id) throws SQLException, ClassNotFoundException {
        RoomDao roomDao = new RoomDao();
        return roomDao.create(player1Id, player2Id);
    }
}
