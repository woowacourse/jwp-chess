package wooteco.chess.dao.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import wooteco.chess.dto.PlayerDto;
import wooteco.chess.dto.RoomDto2;

@SpringBootTest
class RoomRepositoryTest {
	private PlayerDto firstPlayer;
	private PlayerDto secondPlayer;
	private PlayerDto savePlayer1;
	private PlayerDto savePlayer2;
	private RoomDto2 roomDto2;
	private RoomDto2 saveRoom;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private PlayerRepository playerRepository;

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
		assertThat(saveRoom).isNotNull();
		assertThat(saveRoom.getTurnId()).isEqualTo(savePlayer1.getId());
	}

	@Test
	void findById() {
		final RoomDto2 findRoom = roomRepository.findById(saveRoom.getId()).get();
		assertThat(findRoom.getPlayer2Id()).isEqualTo(saveRoom.getPlayer2Id());
	}

	@Test
	void updateTurn() {
		roomRepository.updateTurn(roomDto2.getId(), savePlayer2.getId());
		Optional<RoomDto2> room = roomRepository.findById(roomDto2.getId());
		assertThat(room.get().getTurnId()).isEqualTo(savePlayer2.getId());
	}
}