package chess.repository;

import chess.model.board.Board;
import chess.model.piece.Initializer;
import chess.model.piece.Team;
import chess.model.status.Ready;
import chess.model.status.Running;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChessBoardRepositoryTest {

    private final ChessBoardRepository chessBoardRepository = new ChessBoardRepository(new ConnectionManager());
    private Board board;

    @BeforeEach
    void setup() {
        board = chessBoardRepository.save(new Board(new Ready(), Team.WHITE));
    }

    @AfterEach
    void setDown() {
        chessBoardRepository.deleteAll();
    }

    @Test
    void saveTest() {

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
    void initBoard() {
        final Board edenFightingBoard = new Board(new Running(), Team.WHITE);
        Board board = chessBoardRepository.init(edenFightingBoard, Initializer.initialize());

        assertThat(board.getStatus().name()).isEqualTo("running");
    }
}
