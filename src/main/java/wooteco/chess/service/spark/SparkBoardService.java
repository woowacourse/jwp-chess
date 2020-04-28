package wooteco.chess.service.spark;

import java.sql.SQLException;

import org.springframework.stereotype.Service;

import wooteco.chess.dao.BoardDao;
import wooteco.chess.dao.PlayerDao;
import wooteco.chess.dao.RoomDao;
import wooteco.chess.domain.Board;
import wooteco.chess.domain.ChessGame;
import wooteco.chess.domain.Turn;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.state.BoardFactory;
import wooteco.chess.domain.state.Playing;
import wooteco.chess.dto.GameDto;
import wooteco.chess.dto.RoomDto;

@Service
public class SparkBoardService {
	private final BoardDao boardDao;
	private final RoomDao roomDao;
	private final PlayerDao playerDao;

	public SparkBoardService(BoardDao boardDao, RoomDao roomDao, PlayerDao playerDao) {
		this.boardDao = boardDao;
		this.roomDao = roomDao;
		this.playerDao = playerDao;
	}

	public Board create(int roomId) throws SQLException {
		Board board = boardDao.create(roomId, BoardFactory.create());
		return board;
	}

	public GameDto load(int roomId) throws SQLException {
		Board board = boardDao.findByRoomId(roomId);
		RoomDto room = roomDao.findById(roomId);
		Turn turn = playerDao.findTurn(room.getTurnId());
		ChessGame game = new ChessGame(new Playing(board, turn));
		return new GameDto(board.getBoard(), turn, game.status());
	}

	public GameDto move(int roomId, Position source, Position target) throws SQLException {
		Board board = boardDao.findByRoomId(roomId);
		RoomDto room = roomDao.findById(roomId);
		Turn turn = playerDao.findTurn(room.getTurnId());

		ChessGame game = new ChessGame(new Playing(board, turn));
		game.move(source, target);

		Piece sourcePiece = board.getBoard().get(source);
		Piece targetPiece = board.getBoard().get(target);
		boardDao.updateBoard(roomId, source, sourcePiece);
		boardDao.updateBoard(roomId, target, targetPiece);

		updateTurn(roomId, room);
		return new GameDto(game.board().getBoard(), turn, game.status());
	}

	private void updateTurn(int roomId, RoomDto room) throws SQLException {
		int turnId = room.getPlayer1Id();
		if (room.getTurnId() == room.getPlayer1Id()) {
			turnId = room.getPlayer2Id();
		}
		roomDao.updateTurn(roomId, turnId);
	}

	public int createRoom(int player1Id, int player2Id) throws SQLException {
		RoomDao roomDao = new RoomDao();
		return roomDao.create(player1Id, player2Id);
	}
}
