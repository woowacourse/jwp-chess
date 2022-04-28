package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.entity.CommandEntity;
import chess.entity.RoomEntity;
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
    private RoomService roomService;
    private Long roomId;

    @BeforeEach
    void setUp() {
        RoomEntity roomEntity = roomService.create("room1", "1234");
        roomId = roomEntity.getId();
    }

    @DisplayName("입력한 명령어를 저장한다.")
    @Test
    void create() {
        CommandEntity commandEntity = commandService.create(roomId, "move b2 b4");
        assertThat(commandEntity.getCommand()).isEqualTo("move b2 b4");
    }

    //TODO: 리팩토링
//    @DisplayName("id에 해당되는 체스방의 모든 명령어를 가져온다.")
//    @Test
//    void findCommandsById() {
//        commandService.save(new CommandEntity(roomId, "move b2 b4"));
//        commandService.save(new CommandEntity(roomId, "move b7 b5"));
//        commandService.save(new CommandEntity(roomId, "move c2 c4"));
//        List<String> commands = roomService.findCommandsById(roomEntity.getId());
//        assertThat(commands.size()).isEqualTo(3);
//    }
}
