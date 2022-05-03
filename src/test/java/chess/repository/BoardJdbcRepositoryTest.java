package chess.repository;

import static chess.domain.Fixture.E2;
import static chess.domain.Fixture.E4;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.Room;
import chess.domain.board.Board;
import chess.dto.GameCreateRequest;
import chess.dto.MoveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(properties = "spring.config.location=classpath:/application-test.yml")
@Sql("/schema-test.sql")
class BoardJdbcRepositoryTest {

    public static final GameCreateRequest CREATE_FIXTURE = new GameCreateRequest("test room", "password", "white",
            "black");

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    void setup() {
        roomRepository.deleteAll();
        boardRepository.deleteAll();
    }

    @Test
    @DisplayName("Board 생성 테스트")
    void save() {
        Room room = roomRepository.save(CREATE_FIXTURE);
        Board board = boardRepository.save(room.getId());

        assertThat(board.getBoard()).isNotEmpty();
    }

    @Test
    @DisplayName("Board 기물 이동 반영 테스트")
    void moveUpdate() {
        Room room = roomRepository.save(CREATE_FIXTURE);
        Board board = boardRepository.save(room.getId());
        boardRepository.updateMove(new MoveRequest("pw", "E2", "E4", room.getId()));
        Board boardAfterMove = boardRepository.findById(room.getId());

        assertAll(
                () -> assertThat(board.exists(E2)).isTrue(),
                () -> assertThat(board.exists(E4)).isFalse(),
                () -> assertThat(boardAfterMove.exists(E2)).isFalse(),
                () -> assertThat(boardAfterMove.exists(E4)).isTrue()
        );
    }

    @Test
    @DisplayName("Board 삭제 테스트")
    void deleteById() {
        Room room = roomRepository.save(CREATE_FIXTURE);
        Board board = boardRepository.save(room.getId());
        boardRepository.deleteById(room.getId());
        Board boardAfterDelete = boardRepository.findById(room.getId());

        assertAll(
                () -> assertThat(board.getBoard()).isNotEmpty(),
                () -> assertThat(boardAfterDelete.getBoard()).isEmpty()
        );
    }

    @Test
    @DisplayName("Board 전체 삭제 테스트")
    void deleteAll() {
        Room room = roomRepository.save(CREATE_FIXTURE);
        boardRepository.save(room.getId());
        boardRepository.deleteAll();
        Board boardAfterDelete = boardRepository.findById(room.getId());

        assertThat(boardAfterDelete.getBoard()).isEmpty();
    }
}
