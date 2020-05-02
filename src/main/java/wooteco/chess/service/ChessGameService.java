package wooteco.chess.service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import chess.domain.GameResult;
import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.command.MoveCommand;
import chess.domain.piece.Piece;
import chess.dto.BoardDto;
import chess.dto.TurnDto;
import wooteco.chess.entity.BoardEntity;
import wooteco.chess.entity.RoomEntity;
import wooteco.chess.entity.TurnEntity;
import wooteco.chess.repository.BoardRepository;
import wooteco.chess.repository.RoomRepository;
import wooteco.chess.repository.TurnRepository;

@Service
public class ChessGameService {
	private RoomRepository roomRepository;
	private BoardRepository boardRepository;
	private TurnRepository turnRepository;

	public ChessGameService(RoomRepository roomRepository, BoardRepository boardRepository,
		TurnRepository turnRepository) {
		this.roomRepository = roomRepository;
		this.boardRepository = boardRepository;
		this.turnRepository = turnRepository;
	}

	public RoomEntity createRoom(String name) {
		return roomRepository.save(new RoomEntity(name));
	}

	public List<RoomEntity> loadRooms() {
		return roomRepository.findAll();
	}

	public ChessBoard loadBoard(Long roomId) {
		RoomEntity roomEntity = findRoomEntityById(roomId);
		return createBoardFromDb(roomEntity);
	}

	public ChessBoard createNewChessGame(Long roomId) {
		ChessBoard chessBoard = new ChessBoard();
		Map<Position, Piece> board = chessBoard.getBoard();
		Optional<RoomEntity> maybeRoom = roomRepository.findById(roomId);
		RoomEntity emptyRoomEntity = maybeRoom.orElseThrow(
			() -> new NoSuchElementException(String.format("방 번호가 %d인 방이 " +
					"없습니다" +
					".",
				roomId)));

		for (Position position : board.keySet()) {
			emptyRoomEntity.addBoard(new BoardEntity(position.getName(), board.get(position).getName()));
		}
		String currentTeam = TurnDto.from(chessBoard).getCurrentTeam();
		RoomEntity roomEntity = new RoomEntity(emptyRoomEntity, new TurnEntity(currentTeam));
		roomRepository.save(roomEntity);

		return chessBoard;
	}

	public ChessBoard movePiece(Long roomId, String source, String target) {
		RoomEntity loadedRoomEntity = findRoomEntityById(roomId);

		ChessBoard chessBoard = createBoardFromDb(loadedRoomEntity);
		Map<Position, Piece> board = chessBoard.getBoard();
		chessBoard.move(new MoveCommand(String.format("move %s %s", source, target)));

		loadedRoomEntity.deleteAllBoard();
		for (Position position : board.keySet()) {
			loadedRoomEntity.addBoard(
				new BoardEntity(position.getName(), board.get(position).getName())
			);
		}

		RoomEntity roomEntity = new RoomEntity(loadedRoomEntity, new TurnEntity(
			loadedRoomEntity.getTurnEntity().getId(), TurnDto.from(chessBoard).getCurrentTeam()
		));
		roomRepository.save(roomEntity);

		return chessBoard;
	}

	private void endGame() {
		this.boardRepository.deleteAll();
		this.turnRepository.deleteAll();
	}

	public GameResult findWinner(Long roomId) {
		RoomEntity roomEntity = findRoomEntityById(roomId);
		ChessBoard chessBoard = createBoardFromDb(roomEntity);
		GameResult gameResult = chessBoard.createGameResult();
		endGame();
		return gameResult;
	}

	public boolean isGameOver(Long roomId) {
		RoomEntity roomEntity = findRoomEntityById(roomId);
		ChessBoard chessBoard = createBoardFromDb(roomEntity);
		return chessBoard.isGameOver();
	}

	public boolean isNotGameOver(Long roomId) {
		return !isGameOver(roomId);
	}

	private RoomEntity findRoomEntityById(Long roomId) {
		Optional<RoomEntity> maybeRoomEntity = roomRepository.findById(roomId);
		return maybeRoomEntity.orElseThrow(
			() -> new NoSuchElementException(String.format("%d 해당 아이디의 방을 찾을 수 없습니다.", roomId)));
	}

	private ChessBoard createBoardFromDb(RoomEntity roomEntity) {
		BoardDto boardDto = new BoardDto(roomEntity.getBoardEntities());
		TurnDto turnDto = TurnDto.from(roomEntity.getTurnEntity().getTeamName());

		return new ChessBoard(boardDto.createBoard(), turnDto.createTeam());
	}
}
