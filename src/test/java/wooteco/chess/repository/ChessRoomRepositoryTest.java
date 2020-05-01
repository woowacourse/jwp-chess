package wooteco.chess.repository;

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
class ChessRoomRepositoryTest {

    @Autowired
    private ChessRoomRepository chessRoomRepository;

    @DisplayName("체스 방 생성")
    @Test
    void createNewRoom() {
        ChessRoom chessRoom = new ChessRoom("1번 방");
        ChessRoom savedChessRoom = chessRoomRepository.save(chessRoom);

        List<ChessRoom> foundRooms = chessRoomRepository.findAll();

        assertThat(savedChessRoom.getRoomId()).isEqualTo(foundRooms.get(0).getRoomId());
    }

    @DisplayName("체스 방에 명령어 추가")
    @Test
    void update() {
        ChessRoom room1 = new ChessRoom("1번 방");
        room1.addCommand(new MoveCommand("move a2 a4"));
        ChessRoom room2 = chessRoomRepository.save(room1);
        List<MoveCommand> commands1 = room2.getMoveCommand();

        assertThat(commands1.get(0).getCommand()).isEqualTo("move a2 a4");

        room2.addCommand(new MoveCommand("move a7 a5"));
        ChessRoom room3 = chessRoomRepository.save(room2);
        List<MoveCommand> commands2 = room3.getMoveCommand();

        assertThat(commands2.get(0).getCommand()).isEqualTo("move a2 a4");
        assertThat(commands2.get(1).getCommand()).isEqualTo("move a7 a5");
    }
}