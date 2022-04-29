package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.entity.RoomEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("classpath:roomDaoTest.sql")
class RoomDaoImplTest {

    private RoomDao roomDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        roomDao = new RoomDaoImpl(jdbcTemplate);
        roomDao.saveNewRoom("first", "1234");
    }

    @ParameterizedTest
    @DisplayName("중복되는 체스방 이름이 있는지 반환한다.")
    @CsvSource({"first, true", "second, false"})
    void hasDuplicatedName(final String roomName, final boolean expected) {
        //actual
        final boolean actual = roomDao.hasDuplicatedName(roomName);

        //then
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    @DisplayName("이름에 맞는 체스방을 삭제한다.")
    void deleteRoomByName() {
        //given
        final int roomId = 1;
        roomDao.deleteRoomByName(roomId);
        //when then
        assertThatThrownBy(() -> roomDao.findByRoomId(roomId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("턴을 저장한다.")
    void saveTurn() {
        //given
        final String turn = "BLACK";
        final int roomId = 1;
        roomDao.saveTurn(roomId, turn);
        //when
        final String actual = roomDao.findByRoomId(roomId)
                .getTurn();
        //then
        assertThat(actual).isEqualTo(turn);
    }

    @Test
    @DisplayName("게임 상태를 저장한다.")
    void saveGameState() {
        //given
        final int roomId = 1;
        final String gameState = "ready";
        roomDao.saveGameState(roomId, gameState);

        //when
        final String actual = roomDao.findByRoomId(roomId)
                .getGameState();

        //then
        assertThat(actual).isEqualTo(gameState);
    }

    @Test
    @DisplayName("roomId가 일치하는 체스방 정보를 반환한다.")
    void findByRoomId() {
        //when
        final RoomEntity room = roomDao.findByRoomId(1);
        //then
        assertAll(
                () -> assertThat(room.getRoomId()).isEqualTo(1),
                () -> assertThat(room.getGameState()).isEqualTo("ready"),
                () -> assertThat(room.getPassword()).isEqualTo("1234"),
                () -> assertThat(room.getTurn()).isEqualTo("WHITE"),
                () -> assertThat(room.getName()).isEqualTo("first")
        );
    }

    @Test
    @DisplayName("모든 방 정보를 반환한다.")
    void findAllRooms() {
        //given
        roomDao.saveNewRoom("second", "1234");
        roomDao.saveNewRoom("third", "1234");
        //when
        final List<RoomEntity> rooms = roomDao.findAllRooms();
        //then
        assertAll(
                () -> assertThat(rooms.get(0).getName()).isEqualTo("first"),
                () -> assertThat(rooms.get(1).getName()).isEqualTo("second"),
                () -> assertThat(rooms.get(2).getName()).isEqualTo("third"),
                () -> assertThat(rooms.get(0).getRoomId()).isEqualTo(1),
                () -> assertThat(rooms.get(1).getRoomId()).isEqualTo(2),
                () -> assertThat(rooms.get(2).getRoomId()).isEqualTo(3)
        );
    }
}
