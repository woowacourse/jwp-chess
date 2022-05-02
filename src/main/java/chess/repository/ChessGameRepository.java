package chess.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import chess.domain.Color;
import chess.domain.GameState;
import chess.repository.entity.RoomEntity;
import chess.domain.board.Board;
import chess.domain.chessgame.ChessGame;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.exception.IllegalChessRuleException;
import chess.service.PieceFactory;
import chess.repository.entity.PieceEntity;

@Repository
public class ChessGameRepository {

	private final RoomDao roomDao;
	private final BoardDao boardDao;
	private final PieceDao pieceDao;

	public ChessGameRepository(RoomDao roomDao, BoardDao boardDao, PieceDao pieceDao) {
		this.roomDao = roomDao;
		this.boardDao = boardDao;
		this.pieceDao = pieceDao;
	}

	public ChessGame save(ChessGame chessGame) {
		RoomEntity room = new RoomEntity(chessGame.getName(), chessGame.getPassword());
		int roomId = roomDao.save(room);
		Board board = chessGame.getBoard();
		int boardId = boardDao.save(roomId, GameState.from(board));
		pieceDao.saveAll(boardId, board.getPieces());
		return ChessGame.createWithId(roomId, chessGame);
	}

	public Optional<ChessGame> findByName(String name) {
		Optional<RoomEntity> nullableRoom = roomDao.findByName(name);
		if (nullableRoom.isEmpty()) {
			return Optional.empty();
		}
		RoomEntity room = nullableRoom.get();
		return findById(room.getId());
	}

	public Optional<ChessGame> findById(int id) {
		Optional<RoomEntity> nullableRoom = roomDao.findById(id);
		if (nullableRoom.isEmpty()) {
			return Optional.empty();
		}
		RoomEntity room = nullableRoom.get();
		int boardId = boardDao.getBoardIdByRoom(room.getId());
		Board board = new Board(() -> makePieces(boardId));
		ChessGame chessGame = ChessGame.create(room.getName(), room.getPassword(), board);
		return Optional.of(ChessGame.createWithId(room.getId(), chessGame));
	}

	private Map<Position, Piece> makePieces(int boardId) {
		Map<Position, Piece> pieces = new HashMap<>();
		for (PieceEntity pieceEntity : pieceDao.findAll(boardId)) {
			pieces.put(Position.of(pieceEntity.getPosition()), PieceFactory.build(pieceEntity));
		}
		return pieces;
	}

	public void removeById(int id) {
		roomDao.deleteById(id);
	}

	public List<ChessGame> findAll() {
		List<RoomEntity> rooms = roomDao.findAll();
		return rooms.stream()
			.map(room -> getById(room.getId()))
			.collect(Collectors.toList());
	}

	private ChessGame getById(int gameId) {
		return findById(gameId)
			.orElseThrow(() -> new NoSuchElementException("없는 id입니다."));
	}

	public int updateBoard(int id, Board board) {
		boardDao.deleteByRoom(id);
		int boardId = boardDao.save(id, GameState.from(board));
		pieceDao.saveAll(boardId, board.getPieces());
		return boardId;
	}

	public Board getBoardById(int id) {
		int boardId = boardDao.getBoardIdByRoom(id);
		return findBoardByBoardId(boardId);
	}

	public Color getTurnById(int id) {
		int boardId = boardDao.getBoardIdByRoom(id);
		return boardDao.getTurn(boardId);
	}

	public Board findBoardByBoardId(int boardId) {
		Map<Position, Piece> pieces = new HashMap<>();
		for (PieceEntity pieceEntity : pieceDao.findAll(boardId)) {
			pieces.put(Position.of(pieceEntity.getPosition()), PieceFactory.build(pieceEntity));
		}
		Board board = new Board(() -> pieces);
		board.loadTurn(boardDao.getTurn(boardId));
		return board;
	}

	public int getBoardIdById(int id) {
		return boardDao.getBoardIdByRoom(id);
	}

	public Optional<PieceEntity> findPiece(int boardId, String position) {
		return pieceDao.findOne(boardId, position);
	}

	public void updatePiece(int boardId, String source, String target) {
		PieceEntity pieceEntity = pieceDao.findOne(boardId, source)
			.orElseThrow(() -> new IllegalChessRuleException("피스가 존재하지 않습니다."));
		pieceDao.deleteOne(boardId, source);
		findPiece(boardId, target)
			.ifPresentOrElse(
				dto -> pieceDao.updateOne(boardId, target, pieceEntity),
				() -> pieceDao.save(boardId, target, pieceEntity)
			);
		boardDao.updateTurn(boardId, GameState.from(findBoardByBoardId(boardId)));
	}

	public void updateTurn(int boardId, Board board) {
		boardDao.updateTurn(boardId, GameState.from(board));
	}
}
