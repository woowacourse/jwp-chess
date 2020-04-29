package wooteco.chess.repository;

import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    void afterEach() {
        chessRoomRepository.deleteAll();
    }

    @Test
    void save() {
        ChessRoom room1 = new ChessRoom("첫 번째 방");
        ChessRoom savedRoom1 = chessRoomRepository.save(room1);

        Commands commands = new Commands("move a2 a4", room1);

        assertThat(savedRoom1.getRoomName()).isEqualTo("첫 번째 방");
    }
}