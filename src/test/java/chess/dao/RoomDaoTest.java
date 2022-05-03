package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.Room;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RoomDaoTest {
    @Autowired
    RoomDao roomDao;

    @DisplayName("방 목록을 조회할 수 있다.")
    @Test
    void selectAll() {
        long roomNo = roomDao.insert(Room.create("방제목1", "password"));
        List<Room> rooms = roomDao.selectAll();
        assertThat(rooms.get(0).getTitle()).isEqualTo("방제목1");
        roomDao.delete(roomNo);
    }

    @DisplayName("저장한 비밀번호를 조회할 수 있다.")
    @Test
    void loadPassword() {
        long roomNo = roomDao.insert(Room.create("title", "멋들어진비밀번호"));
        assertThat(roomDao.loadPassword(roomNo)).isEqualTo("멋들어진비밀번호");
        roomDao.delete(roomNo);
    }

    @DisplayName("저장한 제목를 조회할 수 있다.")
    @Test
    void loadTitle() {
        long roomNo = roomDao.insert(Room.create("끝내주는제목", "password"));
        assertThat(roomDao.loadTitle(roomNo)).isEqualTo("끝내주는제목");
        roomDao.delete(roomNo);
    }
}
