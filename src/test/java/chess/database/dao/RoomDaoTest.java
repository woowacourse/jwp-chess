package chess.database.dao;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import chess.database.entity.RoomEntity;

@JdbcTest
public class RoomDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private RoomDao roomDao;
    private Long roomId;

    @BeforeEach
    void setUp() {
        roomDao = new JdbcRoomDao(dataSource, jdbcTemplate);
        roomId = roomDao.saveRoom(RoomEntity.from("TEST-ROOM-NAME", "TEST-PASSWORD"));
    }

    @Test
    @DisplayName("방을 저장한다.")
    public void saveRoom() {
        // given
        RoomEntity entity = RoomEntity.from("TEST-ROOM-NAME2", "TEST-PASSWORD");
        // when
        final Long saveId = roomDao.saveRoom(entity);
        // then
        assertThat(saveId).isNotEqualTo(roomId);
    }

    @Test
    @DisplayName("ID 로 방을 찾는다.")
    void findRoomByRoomId() {
        // given
        final Optional<RoomEntity> findRoomEntity = roomDao.findRoomById(roomId);

        // when
        final boolean isPresent = findRoomEntity.isPresent();

        // then
        assertThat(isPresent).isTrue();
    }

    @Test
    @DisplayName("모든 방을 찾는다.")
    public void findAllRooms() {
        // given
        final List<RoomEntity> all = roomDao.findAll();
        // when
        final int size = all.size();
        // then
        assertThat(size).isEqualTo(1);
    }

    @Test
    @DisplayName("방을 삭제한다.")
    public void deleteRoom() {
        assertThatCode(() -> roomDao.deleteRoom(roomId)).doesNotThrowAnyException();
    }

    @AfterEach
    void setDown() {
        roomDao.deleteRoom(roomId);
    }
}
