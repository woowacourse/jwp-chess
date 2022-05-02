package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.Room;
import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.dto.GameCreateRequest;
import chess.dto.MoveRequest;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(properties = "spring.config.location=classpath:/application-test.yml")
@Sql("/schema-test.sql")
class BoardJdbcRepositoryTest {

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

    }

    @Test
    @DisplayName("Board Repository CRUD 테스트")
    void crud() {
        final GameCreateRequest gameCreateRequest = new GameCreateRequest("test room", "password", "white", "black");
        Room save = roomRepository.save(gameCreateRequest);
        Room save1 = roomRepository.save(gameCreateRequest);
        final long id = save.getId();
        boardRepository.save(id);

        final Board found = boardRepository.findById(id);
        final Map<Position, Piece> board = found.getBoard();

        final boolean a2before = found.exists(Position.from("A2"));
        final boolean a4before = found.exists(Position.from("A4"));
        boardRepository.updateMove(new MoveRequest("pw", "a2", "a4", (int) id));
        final Board foundAfterMove = boardRepository.findById(id);
        final boolean a2after = foundAfterMove.exists(Position.from("A2"));
        final boolean a4after = foundAfterMove.exists(Position.from("A4"));

        boardRepository.deleteById(id);
        final Board notFound = boardRepository.findById(id);
        final Map<Position, Piece> notFoundBoard = notFound.getBoard();

        assertAll(
                () -> assertThat(board.isEmpty()).isFalse(),
                () -> assertThat(a2before).isTrue(),
                () -> assertThat(a4before).isFalse(),
                () -> assertThat(a2after).isFalse(),
                () -> assertThat(a4after).isTrue(),
                () -> assertThat(notFoundBoard.isEmpty()).isTrue()
        );
    }
}
