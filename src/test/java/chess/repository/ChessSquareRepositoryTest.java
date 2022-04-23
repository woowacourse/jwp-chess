package chess.repository;

import chess.model.board.Board;
import chess.model.piece.Piece;
import chess.model.piece.Team;
import chess.model.square.File;
import chess.model.square.Rank;
import chess.model.square.Square;
import chess.model.status.Running;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ChessSquareRepositoryTest {

    private final ChessSquareRepository repository = new ChessSquareRepository(new ConnectionManager());
    @Autowired
    private ChessBoardRepository chessBoardRepository;
    private int boardId;
    private Square square;

    @BeforeEach
    void setup() {
        final Board board = chessBoardRepository.save(new Board(new Running(), Team.WHITE));
        this.boardId = board.getId();
        this.square = repository.save(new Square(File.A, Rank.TWO, boardId));
    }

    @AfterEach
    void setDown() {
        chessBoardRepository.deleteAll();
    }

    @Test
    void save() {
        final Square square = repository.save(new Square(File.B, Rank.TWO, boardId));

        assertAll(
                () -> assertThat(square.getFile()).isEqualTo(File.B),
                () -> assertThat(square.getRank()).isEqualTo(Rank.TWO)
        );
    }

    @Test
    void getBySquareTest() {
        final Square square = repository.getBySquareAndBoardId(new Square(File.A, Rank.TWO), boardId);

        assertAll(
                () -> assertThat(square.getFile()).isEqualTo(File.A),
                () -> assertThat(square.getRank()).isEqualTo(Rank.TWO)
        );
    }

    @Test
    void saveAllSquares() {
        int saveCount = repository.saveAllSquare(boardId);

        assertThat(saveCount).isEqualTo(64);
    }

    @Test
    void getSquareIdBySquare() {
        int squareId = repository.getSquareIdBySquare(new Square(File.A, Rank.TWO), boardId);

        assertThat(squareId).isEqualTo(square.getId());
    }

    @Test
    void findAllSquaresAndPieces() {
        Map<Square, Piece> all = repository.findAllSquaresAndPieces(boardId);

        for (Square Square : all.keySet()) {
            assertThat(all.get(Square).name()).isEqualTo("p");
        }
    }
}
