package wooteco.chess.dao.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import wooteco.chess.dao.util.BoardMapper;
import wooteco.chess.domain.state.BoardFactory;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.dto.PlayerDto;
import wooteco.chess.dto.RoomDto2;

@SpringBootTest
class BoardRepositoryTest {
	PlayerDto firstPlayer;
	PlayerDto secondPlayer;
	PlayerDto savePlayer1;
	PlayerDto savePlayer2;
	RoomDto2 roomDto2;
	RoomDto2 saveRoom;

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private RoomRepository roomRepository;

	@BeforeEach
	void setUp() {
		firstPlayer = new PlayerDto("a", "b", "white");
		secondPlayer = new PlayerDto("c", "d", "black");

		savePlayer1 = playerRepository.save(firstPlayer);
		savePlayer2 = playerRepository.save(secondPlayer);

		roomDto2 = new RoomDto2(savePlayer1.getId(), savePlayer1.getId(), savePlayer2.getId());
		saveRoom = roomRepository.save(roomDto2);
	}

	@Test
	void create() {
		BoardDto boardDto = new BoardDto("p", "white", "a2");
		boardDto.setRoomId(roomDto2.getId());
		BoardDto saveBoard = boardRepository.save(boardDto);

		assertThat(saveBoard).isNotNull();
		assertThat(saveBoard.getPiecePosition()).isEqualTo(boardDto.getPiecePosition());
	}

	@Test
	void update() {
		BoardDto boardDto = new BoardDto("p", "white", "a2");
		boardDto.setRoomId(roomDto2.getId());
		BoardDto saveBoard = boardRepository.save(boardDto);
		saveBoard.setPieceName("k");
		BoardDto updatedBoard = boardRepository.save(saveBoard);

		assertThat(saveBoard.getId()).isEqualTo(updatedBoard.getId());
		assertThat(updatedBoard.getPieceName()).isEqualTo("k");
	}

	@Test
	void findByRoomId() {
		List<BoardDto> pieces = BoardMapper.createMappers(BoardFactory.create());
		for (BoardDto piece : pieces) {
			piece.setRoomId(roomDto2.getId());
			boardRepository.save(piece);
		}
		assertThat(boardRepository.findByRoomId(roomDto2.getId())).hasSize(64);
	}
}