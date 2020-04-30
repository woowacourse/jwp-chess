package wooteco.chess.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJdbcTest
@Transactional
class MoveCommandRepositoryTest {
    @Autowired
    MoveCommandRepository moveCommandRepository;
    @Autowired
    ChessRoomRepository chessRoomRepository;
    ChessRoom savedRoom;

    @BeforeEach
    void setUp() {
        MoveCommand command1 = new MoveCommand("move a2 a4");
        MoveCommand command2 = new MoveCommand("move a7 a5");

        ChessRoom room = new ChessRoom("체스 게임방");
        room.addCommand(command1);
        room.addCommand(command2);
        savedRoom = chessRoomRepository.save(room);
    }

    @DisplayName("체스 게임방에서 이동 기록 가져오기")
    @Test
    void findByRoomId() {
        List<MoveCommand> commands = savedRoom.getMoveCommand();
        assertThat(commands.get(0).getCommand()).isEqualTo("move a2 a4");
        assertThat(commands.get(1).getCommand()).isEqualTo("move a7 a5");

//        List<MoveCommand> commands2 = moveCommandRepository.findAllByRoomId(savedRoom.getRoomId());
//        assertThat(commands2.get(0).getCommand()).isEqualTo("move a2 a4");
    }
}