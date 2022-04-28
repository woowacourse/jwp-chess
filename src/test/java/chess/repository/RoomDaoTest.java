package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;

import chess.entity.RoomEntity;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class RoomDaoTest {

    @Autowired
    private RoomDao roomDao;

    @DisplayName("체스방을 추가한다.")
    @Test
    void insert() {
        //given, when
        final RoomEntity roomEntity = new RoomEntity(1L, "room1", "1234");
        final RoomEntity insertRoom = roomDao.insert(roomEntity);
        // then
        assertThat(insertRoom).isEqualTo(roomEntity);
    }

    @DisplayName("id로 체스방을 조회한다.")
    @Test
    void findById() {
        // given
        final RoomEntity expected = roomDao.insert(new RoomEntity(1L, "room1", "1234"));
        // when
        RoomEntity actual = roomDao.findById(expected.getId());
        // then
        assertThat(actual.getName()).isEqualTo("room1");
        assertThat(actual.getPassword()).isEqualTo("1234");
    }

    @DisplayName("모든 체스방을 조회한다.")
    @Test
    void findAll() {
        // given
        roomDao.insert(new RoomEntity(1L, "room1", "1234"));
        roomDao.insert(new RoomEntity(2L, "room2", "1234"));
        roomDao.insert(new RoomEntity(3L, "room3", "1234"));

        //given, when
        final List<RoomEntity> rooms = roomDao.findAll();
        // then
        assertThat(rooms.size()).isEqualTo(3);
    }

    @DisplayName("해당 id를 가진 체스방을 삭제한다.")
    @Test
    void deleteById() {
        // given
        RoomEntity room1 = roomDao.insert(new RoomEntity(1L, "room1", "1234"));
        // when
        roomDao.deleteById(room1.getId());
        //then
        assertThat(roomDao.findAll()).isEmpty();
    }
}
