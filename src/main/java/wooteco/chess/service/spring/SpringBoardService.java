package wooteco.chess.service.spring;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.chess.dao.repository.BoardRepository;
import wooteco.chess.dao.repository.PlayerRepository;
import wooteco.chess.dao.repository.RoomRepository;
import wooteco.chess.dao.util.BoardMapper;
import wooteco.chess.domain.Board;
import wooteco.chess.domain.ChessGame;
import wooteco.chess.domain.Turn;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.state.BoardFactory;
import wooteco.chess.domain.state.Playing;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.dto.GameDto;
import wooteco.chess.dto.RoomDto2;

@Service
public class SpringBoardService {
	private final BoardRepository boardRepository;
	private final RoomRepository roomRepository;
	private final PlayerRepository playerRepository;

	public SpringBoardService(BoardRepository boardRepository, RoomRepository roomRepository,
		PlayerRepository playerRepository) {
		this.boardRepository = boardRepository;
		this.roomRepository = roomRepository;
		this.playerRepository = playerRepository;
	}

	public Board create(long roomId) {
		Board board = BoardFactory.create();
		List<BoardDto> boardDtos = BoardMapper.createMappers(board);
		for (BoardDto boardDto : boardDtos) {
			boardDto.setRoomId(roomId);
			boardRepository.save(boardDto);
		}
		return board;
	}

	public GameDto load(long roomId) {
		List<BoardDto> boardDto = boardRepository.findByRoomId(roomId);
		Board board = BoardMapper.create(boardDto);
		RoomDto2 room = roomRepository.findById(roomId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방 번호입니다."));
		Turn turn = Turn.from(playerRepository.findById(room.getTurnId()).get().getTeam());
		ChessGame chessGame = new ChessGame(new Playing(board, turn));
		return new GameDto(board.getBoard(), turn, chessGame.status());
	}

	public GameDto move(long roomId, Position source, Position target) {
		Board board = BoardMapper.create(boardRepository.findByRoomId(roomId));
		RoomDto2 room = roomRepository.findById(roomId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방 번호입니다."));
		Turn turn = Turn.from(playerRepository.findById(room.getTurnId()).get().getTeam());

		ChessGame game = new ChessGame(new Playing(board, turn));
		game.move(source, target);

		Piece sourcePiece = board.getBoard().get(source);
		Piece targetPiece = board.getBoard().get(target);

		boardRepository.updatePiece(roomId, source.getName(), sourcePiece.getTeam().name(), sourcePiece.getSymbol());
		boardRepository.updatePiece(roomId, target.getName(), targetPiece.getTeam().name(), targetPiece.getSymbol());

		updateTurn(roomId, room);
		return new GameDto(game.board().getBoard(), turn, game.status());
	}

	private void updateTurn(long roomId, RoomDto2 room) {
		long turnId = room.getPlayer1Id();
		if (room.getTurnId() == room.getPlayer1Id()) {
			turnId = room.getPlayer2Id();
		}
		roomRepository.updateTurn(roomId, turnId);
	}

	public long createRoom(long player1Id, long player2Id) {
		RoomDto2 room = roomRepository.save(new RoomDto2(player1Id, player1Id, player2Id));
		return room.getId();
	}
}
