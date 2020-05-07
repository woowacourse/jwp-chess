package wooteco.chess.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import wooteco.chess.domain.GameResult;
import wooteco.chess.domain.board.ChessBoard;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.command.MoveCommand;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceColor;
import wooteco.chess.domain.piece.PieceType;
import wooteco.chess.dto.request.MoveRequestDto;
import wooteco.chess.dto.response.ChessGameParser;
import wooteco.chess.dto.response.GameResultParser;
import wooteco.chess.entity.BoardEntity;
import wooteco.chess.entity.RoomEntity;
import wooteco.chess.entity.TurnEntity;
import wooteco.chess.repository.RoomRepository;

@Service
public class ChessGameService {
	private RoomRepository roomRepository;

	public ChessGameService(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	public RoomEntity createRoom(String name) {
		return roomRepository.save(new RoomEntity(name));
	}

	public List<RoomEntity> loadRooms() {
		return roomRepository.findAll();
	}

	public ChessGameParser loadBoard(Long roomId) {
		ChessBoard chessBoard = createBoardById(roomId);
		return new ChessGameParser(chessBoard, roomId);
	}

	public ChessGameParser createNewChessGame(Long roomId) {
		ChessBoard chessBoard = new ChessBoard();
		Map<Position, Piece> board = chessBoard.getBoard();
		RoomEntity emptyRoomEntity = findRoomEntityById(roomId);

		for (Position position : board.keySet()) {
			emptyRoomEntity.addBoard(new BoardEntity(position.getName(), board.get(position).getName()));
		}
		String currentTeam = chessBoard.getTeam().getName();
		RoomEntity roomEntity = new RoomEntity(emptyRoomEntity, new TurnEntity(currentTeam));
		roomRepository.save(roomEntity);

		return new ChessGameParser(chessBoard, roomId);
	}

	public ChessGameParser movePiece(MoveRequestDto moveRequestDto) {
		RoomEntity loadedRoomEntity = findRoomEntityById(moveRequestDto.getRoomId());
		ChessBoard chessBoard = createBoardById(moveRequestDto.getRoomId());

		Map<Position, Piece> board = chessBoard.getBoard();
		chessBoard.move(
			new MoveCommand(String.format("move %s %s", moveRequestDto.getSource(), moveRequestDto.getTarget())));

		loadedRoomEntity.deleteAllBoard();
		for (Position position : board.keySet()) {
			loadedRoomEntity.addBoard(
				new BoardEntity(position.getName(), board.get(position).getName())
			);
		}

		RoomEntity roomEntity = new RoomEntity(loadedRoomEntity, new TurnEntity(
			loadedRoomEntity.getTurnEntity().getId(), chessBoard.getTeam().getName()
		));
		roomRepository.save(roomEntity);

		return new ChessGameParser(chessBoard, moveRequestDto.getRoomId());
	}

	private void endGame(Long roomId) {
		this.roomRepository.deleteById(roomId);
	}

	public GameResultParser findWinner(Long roomId) {
		ChessBoard chessBoard = createBoardById(roomId);
		GameResult gameResult = chessBoard.createGameResult();
		endGame(roomId);
		return new GameResultParser(gameResult);
	}

	public boolean isGameOver(Long roomId) {
		ChessBoard chessBoard = createBoardById(roomId);
		return chessBoard.isGameOver();
	}

	public boolean isNotGameOver(Long roomId) {
		return !isGameOver(roomId);
	}

	public void deleteGame(Long roomId) {
		roomRepository.deleteById(roomId);
	}

	private RoomEntity findRoomEntityById(Long roomId) {
		return roomRepository.findById(roomId)
			.orElseThrow(() -> new IllegalArgumentException(String.format("%d 해당 아이디의 방을 찾을 수 없습니다.", roomId)));
	}

	private ChessBoard createBoardById(Long roomId) {
		RoomEntity roomEntity = findRoomEntityById(roomId);

		if (roomEntity.isEmptyBoard() && roomEntity.isEmptyTurn()) {
			throw new IllegalArgumentException("새 게임을 눌러주세요!");
		}

		Map<Position, Piece> board = new HashMap<>();

		for (BoardEntity boardEntity : roomEntity.getBoardEntities()) {
			Position position = new Position(boardEntity.getPosition());
			Piece piece = PieceType.of(boardEntity.getPieceName()).createPiece(position);
			board.put(position, piece);
		}

		PieceColor currentTeam = PieceColor.of(roomEntity.getTurnEntity().getTeamName());
		return new ChessBoard(board, currentTeam);
	}
}
