package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;

import chess.model.board.Board;
import chess.model.piece.Team;
import chess.model.status.End;
import chess.model.status.Ready;
import chess.model.status.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@DataJdbcTest
@Sql("test-schema.sql")
class ChessBoardRepositoryTest {

    private final ChessBoardRepository chessBoardRepository;

    @Autowired
    ChessBoardRepositoryTest(JdbcTemplate jdbcTemplate) {
        chessBoardRepository = new ChessBoardRepository(jdbcTemplate);
    }

    @Test
    void saveTest() {
        //when
        Board board = chessBoardRepository.save(new Board(new Ready(), Team.BLACK));

        //then
        assertThat(board).isNotNull();
    }

    @Test
    void getByIdTest() {
        //given
        Board board = chessBoardRepository.save(new Board(new Ready(), Team.BLACK));
        //when
        final Board foundBoard = chessBoardRepository.getById(board.getId());

        //then
        assertThat(foundBoard.getStatus()).isInstanceOf(Ready.class);
    }

    @Test
    void updateStatus() {
        //given
        Board board = chessBoardRepository.save(new Board(new Ready(), Team.BLACK));
        //when
        int affectedRowCount = chessBoardRepository.updateStatus(board.getId(), new End());

        //then
        assertThat(affectedRowCount).isEqualTo(1);
    }

    @Test
    void updateTeam() {
        //given
        Board board = chessBoardRepository.save(new Board(new Ready(), Team.BLACK));
        //when
        int affectedRowCount = chessBoardRepository.updateTeamById(board.getId(), Team.BLACK);

        //then
        assertThat(affectedRowCount).isEqualTo(1);
    }

    @Test
    void getStatusById() {
        //given
        Board board = chessBoardRepository.save(new Board(new Ready(), Team.BLACK));
        //when
        Status status = chessBoardRepository.getStatusById(board.getId());

        //then
        assertThat(status.name()).isEqualTo("ready");
    }
}
