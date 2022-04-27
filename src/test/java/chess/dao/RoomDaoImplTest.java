package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.dto.RoomDto;
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
    @DisplayName("체스방 번호에 따른 비밀번호를 반환한다.")
    void getPasswordByName() {
        //given
        final int roomId = 1;
        final String password = "1234";

        //when
        final String actual = roomDao.getPasswordByName(roomId);

        //then
        assertThat(actual).isEqualTo(password);
    }

    @Test
    @DisplayName("체스방 이름에 따른 게임 진행 상태를 반환한다.")
    void getGameStateByName() {
        //given
        final int roomId = 1;
        final String expected = "ready";

        //when
        final String actual = roomDao.getGameStateByName(roomId);
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
        assertThatThrownBy(() -> roomDao.getGameStateByName(roomId))
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
        final String actual = roomDao.getTurn(roomId);
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
        final String actual = roomDao.getGameStateByName(roomId);

        //then
        assertThat(actual).isEqualTo(gameState);
    }

    @Test
    @DisplayName("방들의 이름을 반환한다.")
    void getRoomNames() {
        //given
        roomDao.saveNewRoom("second", "1234");
        roomDao.saveNewRoom("third", "1234");
        //when
        final List<RoomDto> roomNames = roomDao.getRoomNames();
        //then
        assertAll(
                () -> assertThat(roomNames.get(0).getRoomName()).isEqualTo("first"),
                () -> assertThat(roomNames.get(1).getRoomName()).isEqualTo("second"),
                () -> assertThat(roomNames.get(2).getRoomName()).isEqualTo("third"),
                () -> assertThat(roomNames.get(0).getRoomId()).isEqualTo(1),
                () -> assertThat(roomNames.get(1).getRoomId()).isEqualTo(2),
                () -> assertThat(roomNames.get(2).getRoomId()).isEqualTo(3)
        );
    }
}
