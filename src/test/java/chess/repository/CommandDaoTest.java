package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;

import chess.entity.CommandEntity;
import chess.entity.RoomEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class CommandDaoTest {

    @Autowired
    private CommandDao commandDao;

    @Autowired
    private RoomDao roomDao;

    @DisplayName("체스방에 실행한 명령어를 저장한다.")
    @Test
    void insert() {
        RoomEntity roomEntity = roomDao.insert(new RoomEntity(1L, "room1", "1234"));
        CommandEntity expected = new CommandEntity(1L, roomEntity.getId(), "move b2 b4");
        CommandEntity actual = commandDao.insert(expected);
        assertThat(actual.getRoomId()).isEqualTo(roomEntity.getId());
        assertThat(actual.getCommand()).isEqualTo("move b2 b4");
    }

    @DisplayName("체스방에 실행된 모든 명령어를 가져온다.")
    @Test
    void findAll() {
        RoomEntity roomEntity = roomDao.insert(new RoomEntity(1L, "room1", "1234"));
        commandDao.insert(new CommandEntity(1L, roomEntity.getId(), "move b2 b4"));
        commandDao.insert(new CommandEntity(1L, roomEntity.getId(), "move b7 b5"));
        commandDao.insert(new CommandEntity(1L, roomEntity.getId(), "move c2 b4"));
        assertThat(commandDao.findAll().size()).isEqualTo(3);
    }
}
