package chess.repository;

import chess.model.board.Board;
import chess.model.piece.Team;
import chess.model.status.End;
import chess.model.status.Ready;
import chess.model.status.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ChessBoardRepositoryTest {

    @Autowired
    private ChessBoardRepository chessBoardRepository;
    private Board board;

    @BeforeEach
    void setup() {
        board = chessBoardRepository.save(new Board(new Ready(), Team.WHITE));
    }

    @Test
    void test() {
        chessBoardRepository.test(69);
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

//    @Test
//    void initBoard() {
//        final Board edenFightingBoard = new Board(new Running(), Team.WHITE);
//        Board board = chessBoardRepository.init(edenFightingBoard, Initializer.initialize());
//
//        assertThat(board.getStatus().name()).isEqualTo("running");
//    }

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
