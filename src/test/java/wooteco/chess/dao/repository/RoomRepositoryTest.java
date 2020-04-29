package wooteco.chess.dao.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import wooteco.chess.db.entity.PlayerEntity;
import wooteco.chess.db.entity.RoomEntity;
import wooteco.chess.db.repository.PlayerRepository;
import wooteco.chess.db.repository.RoomRepository;

@SpringBootTest
class RoomRepositoryTest {
	private PlayerEntity firstPlayer;
	private PlayerEntity secondPlayer;
	private PlayerEntity savePlayer1;
	private PlayerEntity savePlayer2;
	private RoomEntity roomEntity;
	private RoomEntity saveRoom;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private PlayerRepository playerRepository;

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
		assertThat(saveRoom).isNotNull();
		assertThat(saveRoom.getTurnId()).isEqualTo(savePlayer1.getId());
	}

	@Test
	void findById() {
		final RoomEntity findRoom = roomRepository.findById(saveRoom.getId()).get();
		assertThat(findRoom.getPlayer2Id()).isEqualTo(saveRoom.getPlayer2Id());
	}

	@Test
	void updateTurn() {
		roomRepository.updateTurn(roomEntity.getId(), savePlayer2.getId());
		Optional<RoomEntity> room = roomRepository.findById(roomEntity.getId());
		assertThat(room.get().getTurnId()).isEqualTo(savePlayer2.getId());
	}
}