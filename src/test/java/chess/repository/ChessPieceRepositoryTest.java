package chess.repository;

import chess.model.board.Board;
import chess.model.piece.Pawn;
import chess.model.piece.Piece;
import chess.model.piece.Team;
import chess.model.square.File;
import chess.model.square.Rank;
import chess.model.square.Square;
import chess.model.status.Running;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJdbcTest
@Sql("test-schema.sql")
class ChessPieceRepositoryTest {

    private final ChessPieceRepository chessPieceRepository;

    private final ChessSquareRepository chessSquareRepository;

    private final ChessBoardRepository chessBoardRepository;
    private Board board;
    private Square square;

    @Autowired
    ChessPieceRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.chessPieceRepository = new ChessPieceRepository(jdbcTemplate);
        this.chessSquareRepository = new ChessSquareRepository(jdbcTemplate);
        this.chessBoardRepository = new ChessBoardRepository(jdbcTemplate);
    }

    @BeforeEach
    void setup() {
        board = chessBoardRepository.save(new Board(new Running(), Team.WHITE));
        square = chessSquareRepository.save(new Square(File.A, Rank.TWO, board.getId()));
    }

    @Test
    void saveTest() {
        //when
        final Piece piece = chessPieceRepository.save(new Pawn(Team.WHITE), square.getId());

        //then
        assertAll(
                () -> assertThat(piece.name()).isEqualTo("p"),
                () -> assertThat(piece.team()).isEqualTo(Team.WHITE)
        );
    }

    @Test
    void findBySquareId() {
        //given
        chessPieceRepository.save(new Pawn(Team.WHITE), square.getId());
        //when
        Piece piece = chessPieceRepository.findBySquareId(square.getId());

        //then
        assertAll(
                () -> assertThat(piece.name()).isEqualTo("p"),
                () -> assertThat(piece.team()).isEqualTo(Team.WHITE)
        );
    }

    @Test
    void deletePieceBySquareId() {
        //given
        chessPieceRepository.save(new Pawn(Team.WHITE), square.getId());
        //when
        int affectedRows = chessPieceRepository.deletePieceBySquareId(square.getId());

        //then
        assertThat(affectedRows).isEqualTo(1);
    }

    @Test
    void updatePieceSquareId() {
        //given
        chessPieceRepository.save(new Pawn(Team.WHITE), square.getId());
        final int originalSquareId = square.getId();
        final int newSquareId = chessSquareRepository.save(new Square(File.A, Rank.FOUR, board.getId())).getId();

        //when
        int affectedRow = chessPieceRepository.updatePieceSquareId(originalSquareId, newSquareId);

        //then
        assertThat(affectedRow).isEqualTo(1);
    }

    @Test
    void getAllPiecesByBoardId() {
        //given
        chessPieceRepository.save(new Pawn(Team.WHITE), square.getId());
        //when
        List<Piece> pieces = chessPieceRepository.getAllPiecesByBoardId(board.getId());

        //then
        assertThat(pieces.size()).isEqualTo(1);
    }
}
