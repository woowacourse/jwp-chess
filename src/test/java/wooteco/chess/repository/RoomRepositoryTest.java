// package wooteco.chess.repository;
//
// import static org.assertj.core.api.Assertions.*;
//
// import java.util.List;
//
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
// import org.springframework.test.context.junit.jupiter.SpringExtension;
// import org.springframework.transaction.annotation.Transactional;
//
// @ExtendWith(SpringExtension.class)
// @DataJdbcTest
// @Transactional
// public class RoomRepositoryTest {
// 	public static final String NAME = "브라운";
// 	public static final String NEW_NAME = "뉴브라운";
//
// 	@Autowired
// 	private RoomRepository roomRepository;
//
// 	@DisplayName("방 전체 목록 조회")
// 	@Test
// 	public void findAll() {
// 		roomRepository.save(new Room(NAME));
// 		roomRepository.save(new Room(NEW_NAME));
//
// 		List<Room> rooms = roomRepository.findAll();
// 		assertThat(rooms.size()).isEqualTo(2);
// 	}
//
// 	@DisplayName("방 생성")
// 	@Test
// 	public void save() {
// 		Room savedRoom = roomRepository.save(new Room(NAME));
//
// 		assertThat(savedRoom.getRoomId()).isNotNull();
// 		assertThat(savedRoom.getRoomName()).isEqualTo(NAME);
// 		assertThat(savedRoom.getBoard()).isNotNull();
// 		assertThat(savedRoom.getTurn()).isNotNull();
// 		assertThat(savedRoom.getFinishFlag()).isNotNull();
// 	}
//
// 	@DisplayName("방 수정")
// 	@Test
// 	public void update() {
// 		Room savedRoom = roomRepository.save(new Room(NAME));
// 		savedRoom.update(new Room(NEW_NAME));
// 		Room updatedRoom = roomRepository.save(savedRoom);
//
// 		Room persistRoom = roomRepository.findByRoomName(updatedRoom.getRoomName()).orElseThrow(RuntimeException::new);
//
// 		assertThat(persistRoom.getRoomName()).isEqualTo(NEW_NAME);
// 	}
//
// 	@DisplayName("방 이름으로 조회")
// 	@Test
// 	public void findByRoomName() {
// 		Room savedRoom = roomRepository.save(new Room(NAME));
// 		assertThat(savedRoom.getRoomId()).isNotNull();
//
// 		Room persistRoom = roomRepository.findById(savedRoom.getRoomId()).orElseThrow(RuntimeException::new);
// 		assertThat(persistRoom.getRoomName()).isEqualTo(NAME);
// 	}
//
// }
//
