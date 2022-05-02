package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.entity.CommandEntity;
import chess.entity.RoomEntity;
import chess.exception.DeleteRoomException;
import chess.repository.RoomDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class CommandServiceTest {

    @Autowired
    private CommandService commandService;

    @Autowired
    private RoomDao roomDao;
    private Long roomId;

    @BeforeEach
    void setUp() {
        RoomEntity roomEntity = roomDao.insert(new RoomEntity("room1", "1234"));
        roomId = roomEntity.getId();
    }

    @DisplayName("입력한 명령어를 저장한다.")
    @Test
    void create() {
        CommandEntity commandEntity = commandService.create(roomId, "move b2 b4");
        assertThat(commandEntity.getCommand()).isEqualTo("move b2 b4");
    }

    @DisplayName("id에 해당되는 체스방의 모든 명령어를 가져온다.")
    @Test
    void findCommandsById() {
        commandService.create(roomId, "move b2 b4");
        commandService.create(roomId, "move b7 b5");
        commandService.create(roomId, "move c2 c4");
        assertThat(commandService.findAllByRoomID(roomId)).containsExactly("move b2 b4", "move b7 b5", "move c2 c4");
    }

    @DisplayName("id에 해당되는 방이 실행중인지 검증한다.")
    @Test
    void checkRunning() {
        RoomEntity room1 = roomDao.insert(new RoomEntity("room1", "1234"));
        assertThatThrownBy(() -> commandService.checkRunning(room1.getId()))
                .isInstanceOf(DeleteRoomException.class);
    }
}
