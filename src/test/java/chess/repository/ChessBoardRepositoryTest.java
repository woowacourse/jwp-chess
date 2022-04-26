package chess.repository;

import chess.model.board.Board;
import chess.model.piece.Team;
import chess.model.status.End;
import chess.model.status.Ready;
import chess.model.status.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Sql("test-schema.sql")
class ChessBoardRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ChessBoardRepository chessBoardRepository;
    private Board board;

    @BeforeEach
    void setup() {
        this.chessBoardRepository = new ChessBoardRepository(jdbcTemplate);
        board = chessBoardRepository.save(new Board(new Ready(), Team.WHITE));
    }

    @Test
    void saveTest() {
        board = chessBoardRepository.save(new Board(new Ready(), Team.BLACK));
        assertThat(board.getStatus()).isInstanceOf(Ready.class);
    }

    @Test
    void deleteBoard() {
        int affectedRow = chessBoardRepository.deleteById(board.getId());

        assertThat(affectedRow).isEqualTo(1);
    }

    @Test
    void getByIdTest() {
        final Board foundBoard = chessBoardRepository.getById(board.getId());

        assertThat(foundBoard.getStatus()).isInstanceOf(Ready.class);
    }

    @Test
    void updateStatus() {
        chessBoardRepository.updateStatus(board.getId(), new End());
        Board dbBoard = chessBoardRepository.getById(this.board.getId());
        assertThat(dbBoard.getStatus()).isInstanceOf(End.class);
    }

    @Test
    void updateTeam() {
        chessBoardRepository.updateTeamById(board.getId(), Team.BLACK);
        Board dbBoard = chessBoardRepository.getById(this.board.getId());
        assertThat(dbBoard.getTeam()).isEqualTo(Team.BLACK);
    }

    @Test
    void getStatusById() {
        Status status = chessBoardRepository.getStatusById(board.getId());
        assertThat(status.name()).isEqualTo("ready");
    }
}
