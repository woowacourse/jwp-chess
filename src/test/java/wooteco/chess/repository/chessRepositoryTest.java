package wooteco.chess.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.chess.entity.Move;
import wooteco.chess.entity.Room;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class chessRepositoryTest {
    @Autowired
    MoveRepository moveRepository;
    @Autowired
    RoomRepository roomRepository;

    @AfterEach
    void deleteAll() {
        moveRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @DisplayName("findByRoomId 잘 작동 하는지 확인")
    @Test
    void findByRoomId() {
        Room room = new Room("blackPassword",
                "whitePassword",
                false,
                "testRoom");
        roomRepository.save(room);
        room = roomRepository.findByName("testRoom").orElse(null);
        moveRepository.save(new Move(room.getId(), "a2", "a4"));
        List<Move> moves = moveRepository.findByRoomId(room.getId()).orElse(null);
        assertThat(moves.get(0).getSource()+moves.get(0).getTarget())
                .isEqualTo("a2a4");
    }
}
