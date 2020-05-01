package wooteco.chess.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJdbcTest
@Transactional
class ChessRoomRepositoryTest {

    @Autowired
    private ChessRoomRepository chessRoomRepository;

    @Test
    void save() {
        ChessRoom room1 = new ChessRoom("첫 번째 방");
        ChessRoom savedRoom1 = chessRoomRepository.save(room1);

        assertThat(savedRoom1.getRoomName()).isEqualTo("첫 번째 방");
    }

    @DisplayName("체스 방 업데이트")
    @Test
    void update() {
        ChessRoom room1 = new ChessRoom("1번 방");
        ChessRoom savedRoom = chessRoomRepository.save(room1);

        ChessRoom foundRoom = chessRoomRepository.findById(savedRoom.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 방 번호입니다."));
        foundRoom.addCommand(new MoveCommand("move a2 a4"));
        ChessRoom room2 = chessRoomRepository.save(foundRoom);

        room2.addCommand(new MoveCommand("move a7 a5"));
        ChessRoom room3 = chessRoomRepository.save(room2);

        assertThat(room3.getMoveCommand().get(0).getCommand()).isEqualTo("move a2 a4");
        assertThat(room3.getMoveCommand().get(1).getCommand()).isEqualTo("move a7 a5");
    }
}