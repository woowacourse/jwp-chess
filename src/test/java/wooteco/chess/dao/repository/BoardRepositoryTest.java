package wooteco.chess.dao.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import wooteco.chess.db.BoardMapper;
import wooteco.chess.db.entity.BoardEntity;
import wooteco.chess.db.entity.PlayerEntity;
import wooteco.chess.db.entity.RoomEntity;
import wooteco.chess.db.repository.BoardRepository;
import wooteco.chess.db.repository.PlayerRepository;
import wooteco.chess.db.repository.RoomRepository;
import wooteco.chess.domain.state.BoardFactory;

@SpringBootTest
class BoardRepositoryTest {
	PlayerEntity firstPlayer;
	PlayerEntity secondPlayer;
	PlayerEntity savePlayer1;
	PlayerEntity savePlayer2;
	RoomEntity roomEntity;
	RoomEntity saveRoom;

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private RoomRepository roomRepository;

	@BeforeEach
	void setUp() {
		firstPlayer = new PlayerEntity("a", "b", "white");
		secondPlayer = new PlayerEntity("c", "d", "black");

		savePlayer1 = playerRepository.save(firstPlayer);
		savePlayer2 = playerRepository.save(secondPlayer);

		roomEntity = new RoomEntity(savePlayer1.getId(), savePlayer1.getId(), savePlayer2.getId());
		saveRoom = roomRepository.save(roomEntity);
	}

	@Test
	void create() {
		BoardEntity boardEntity = new BoardEntity("p", "white", "a2");
		boardEntity.setRoomId(roomEntity.getId());
		BoardEntity saveBoard = boardRepository.save(boardEntity);

		assertThat(saveBoard).isNotNull();
		assertThat(saveBoard.getPiecePosition()).isEqualTo(boardEntity.getPiecePosition());
	}

	@Test
	void update() {
		BoardEntity boardEntity = new BoardEntity("p", "white", "a2");
		boardEntity.setRoomId(roomEntity.getId());
		BoardEntity saveBoard = boardRepository.save(boardEntity);
		saveBoard.setPieceName("k");
		BoardEntity updatedBoard = boardRepository.save(saveBoard);

		assertThat(saveBoard.getId()).isEqualTo(updatedBoard.getId());
		assertThat(updatedBoard.getPieceName()).isEqualTo("k");
	}

	@Test
	void findByRoomId() {
		List<BoardEntity> pieces = BoardMapper.createMappers(BoardFactory.create());
		for (BoardEntity piece : pieces) {
			piece.setRoomId(roomEntity.getId());
			boardRepository.save(piece);
		}
		assertThat(boardRepository.findByRoomId(roomEntity.getId())).hasSize(64);
	}
}