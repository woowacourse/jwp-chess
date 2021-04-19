package chess.database.room;

import chess.repository.room.DuplicateRoomNameException;
import chess.repository.room.NoSuchRoomNameException;
import chess.repository.room.Room;
import chess.repository.room.SpringRoomDao;
import chess.util.JsonConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
class SpringRoomDaoTest {
    private SpringRoomDao springRoomDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        springRoomDao = new SpringRoomDao(jdbcTemplate);

        jdbcTemplate.execute("set mode MySQL;");
        jdbcTemplate.execute("DROP TABLE rooms IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE rooms(" +
                "room_id SERIAL, name VARCHAR(12) UNIQUE, turn VARCHAR(6), state VARCHAR(255))");

        List<Object[]> roomData = Arrays.asList("오즈방 white {a1:pieceInfo1}",
                "삭정방 black {b1:pieceInfo2}",
                "포비방 black {c1:pieceInfo3}",
                "닉방 white {d1:pieceInfo4}").stream()
                .map(name -> name.split(" "))
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate("INSERT INTO rooms(name, turn, state) VALUES (?, ?, ?)", roomData);
    }

    @DisplayName("방 이름 따라서 방 제대로 찾는지")
    @Test
    void findByRoomName() {
        Room sakjungRoom = springRoomDao.findByRoomName("삭정방");
        assertThat(sakjungRoom).isEqualTo(new Room("삭정방", "black", JsonConverter.fromJson("{b1:pieceInfo2}")));
    }

    @DisplayName("존재하지 않는 방 이름일 경우 제대로된 에러를 반환 하는지")
    @Test
    void findByRoomName_NotExistingRoomName_throwError() {
        assertThatThrownBy(() -> springRoomDao.findByRoomName("워니방"))
                .isInstanceOf(NoSuchRoomNameException.class);
    }

    @DisplayName("방 상태 저장 제대로 하는지")
    @Test
    void addRoom() {
        Room room = new Room("씨유방",
                "white",
                JsonConverter.fromJson("{e1:pieceInfo5}"));
        springRoomDao.addRoom(room);

        Room cuRoom = springRoomDao.findByRoomName("씨유방");
        assertThat(cuRoom).isEqualTo(new Room("씨유방", "white", JsonConverter.fromJson("{e1:pieceInfo5}")));
    }

    @DisplayName("존재하는 데이터가 있을 때 데이터를 덮어 씌우는지")
    @Test
    void addRoomOn() {
        Room nickRoom = springRoomDao.findByRoomName("닉방");
        assertThat(nickRoom).isEqualTo(new Room("닉방", "white", JsonConverter.fromJson("{d1:pieceInfo4}")));

        Room room = new Room("닉방",
                "black",
                JsonConverter.fromJson("{a2:pieceInfo1}"));
        springRoomDao.addRoom(room);

        nickRoom = springRoomDao.findByRoomName("닉방");
        assertThat(nickRoom).isEqualTo(new Room("닉방", "black", JsonConverter.fromJson("{a2:pieceInfo1}")));
    }

    @Test
    void validateRoomExistence() {
        assertThatThrownBy(() -> springRoomDao.validateRoomExistence("오즈방"))
                .isInstanceOf(DuplicateRoomNameException.class);
    }

    @Test
    void getAllRoom() {
        List<String> allRoomNames = springRoomDao.getAllRoom();
        List<String> expectedRoomNames = Arrays.asList("오즈방", "삭정방", "포비방", "닉방");
        assertThat(allRoomNames).hasSize(expectedRoomNames.size()).hasSameElementsAs(expectedRoomNames);
    }
}