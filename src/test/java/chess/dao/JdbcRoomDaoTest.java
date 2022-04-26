package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.dao.dto.RoomSaveDto;
import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;
import chess.entity.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class JdbcRoomDaoTest {

    private static final RoomSaveDto generateRoomSaveDto = new RoomSaveDto("매트의 체스", "123123",
            GameStatus.READY, Color.WHITE);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcRoomDao roomDao;

    @BeforeEach
    void setUp() {
        roomDao = new JdbcRoomDao(jdbcTemplate);
    }

    @DisplayName("RoomSaveDto를 전달 받아 room entity를 저장한다.")
    @Test
    void room_저장한다() {
        final RoomSaveDto roomSaveDto = generateRoomSaveDto;

        final int id = roomDao.save(roomSaveDto);

        assertThat(roomDao.findById(id).isPresent()).isTrue();
    }

    @DisplayName("room id를 활용하여 entity를 조회한다.")
    @Test
    void room_조회한다() {
        final RoomSaveDto roomSaveDto = generateRoomSaveDto;
        final int id = roomDao.save(roomSaveDto);

        final Room room = roomDao.findById(id).get();

        assertAll(
                () -> assertThat(id).isEqualTo(room.getId()),
                () -> assertThat("매트의 체스").isEqualTo(room.getName()),
                () -> assertThat("123123").isEqualTo(room.getPassword()),
                () -> assertThat(GameStatus.READY.getValue()).isEqualTo(room.getGameStatus()),
                () -> assertThat(Color.WHITE.getValue()).isEqualTo(room.getCurrentTurn())
        );
    }

    @DisplayName("존재하지 않는 id의 room을 조회한다.")
    @Test
    void room_조회_실패한다() {
        final boolean result = roomDao.findById(26).isEmpty();

        assertThat(result).isTrue();
    }

    @DisplayName("id를 기반으로 room을 삭제한다.")
    @Test
    void room_삭제한다() {
        final RoomSaveDto roomSaveDto = generateRoomSaveDto;
        final int id = roomDao.save(roomSaveDto);

        roomDao.deleteById(id);

        assertThat(roomDao.findById(id).isEmpty()).isTrue();
    }

    @DisplayName("id를 기반으로 room에 존재하지 않는 것을 삭제한다. 특별한 예외는 던지지 않는다.")
    @Test
    void room_삭제실패한다() {
        assertThatCode(() -> roomDao.deleteById(26));
    }
}
