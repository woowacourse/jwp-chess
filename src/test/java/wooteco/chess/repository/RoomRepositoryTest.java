package wooteco.chess.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import wooteco.chess.domain.BoardConverter;
import wooteco.chess.domain.ChessGame;
import wooteco.chess.domain.FinishFlag;
import wooteco.chess.domain.Side;

@ExtendWith(SpringExtension.class)
@DataJdbcTest
@Transactional
public class RoomRepositoryTest {
	private static final String NAME = "room";
	private static final String NEW_NAME = "newRoom";
	private String board;
	private Side turn;
	private String finishFlag;

	@Autowired
	private RoomRepository roomRepository;

	@BeforeEach
	void setUp() {
		board = BoardConverter.convertToString(ChessGame.start().getBoard());
		turn = Side.WHITE;
		finishFlag = FinishFlag.GOING.getSymbol();
	}

	@DisplayName("방 전체 목록 조회")
	@Test
	public void findAll() {
		Room room = new Room(NAME, board, turn, finishFlag);
		roomRepository.save(room);
		roomRepository.save(new Room(NEW_NAME, board, turn, finishFlag));

		List<Room> rooms = roomRepository.findAll();
		assertThat(rooms.size()).isEqualTo(2);
	}

	@DisplayName("방 생성")
	@Test
	public void save() {
		Room savedRoom = roomRepository.save(new Room(NAME, board, turn, finishFlag));

		assertThat(savedRoom.getRoomId()).isNotNull();
		assertThat(savedRoom.getRoomName()).isEqualTo(NAME);
		assertThat(savedRoom.getBoard()).isNotNull();
		assertThat(savedRoom.getTurn()).isNotNull();
		assertThat(savedRoom.getFinishFlag()).isNotNull();
	}

	@DisplayName("방 수정")
	@Test
	public void update() {
		Room savedRoom = roomRepository.save(new Room(NAME, board, turn, finishFlag));
		savedRoom.update(board, Side.BLACK, finishFlag);
		Room updatedRoom = roomRepository.save(savedRoom);

		Room persistRoom = roomRepository.findByRoomName(updatedRoom.getRoomName()).orElseThrow(RuntimeException::new);

		assertThat(persistRoom.getTurn()).isEqualTo(Side.BLACK);
	}

	@DisplayName("방 이름으로 조회")
	@Test
	public void findByRoomName() {
		Room savedRoom = roomRepository.save(new Room(NAME, board, turn, finishFlag));
		assertThat(savedRoom.getRoomId()).isNotNull();

		Room persistRoom = roomRepository.findById(savedRoom.getRoomId()).orElseThrow(RuntimeException::new);
		assertThat(persistRoom.getRoomName()).isEqualTo(NAME);
	}

}

