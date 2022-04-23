package chess.repository;

import chess.model.board.Board;
import chess.model.piece.Pawn;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class ChessPieceRepositoryTest {

    @Autowired
    private ChessPieceRepository chessPieceRepository;

    @Autowired
    private ChessSquareRepository chessSquareRepository;
    @Autowired
    private ChessBoardRepository chessBoardRepository;
    private int boardId;
    private int squareId;

    @BeforeEach
    void setup() {
        final Board board = chessBoardRepository.save(new Board(new Running(), Team.WHITE));
        this.boardId = board.getId();
        final Square square = chessSquareRepository.save(new Square(File.A, Rank.TWO, board.getId()));
        this.squareId = square.getId();
        chessPieceRepository.save(new Pawn(Team.WHITE), squareId);
    }

    @Test
    void saveTest() {
        final Piece piece = chessPieceRepository.save(new Pawn(Team.WHITE), squareId);

        assertAll(
                () -> assertThat(piece.name()).isEqualTo("p"),
                () -> assertThat(piece.team()).isEqualTo(Team.WHITE),
                () -> assertThat(piece.getSquareId()).isEqualTo(squareId)
        );
    }

    @Test
    void findBySquareId() {
        Piece piece = chessPieceRepository.findBySquareId(squareId);

        assertAll(
                () -> assertThat(piece.name()).isEqualTo("p"),
                () -> assertThat(piece.team()).isEqualTo(Team.WHITE)
        );
    }

    @Test
    void deletePieceBySquareId() {
        int affectedRows = chessPieceRepository.deletePieceBySquareId(squareId);

        assertThat(affectedRows).isEqualTo(1);
    }

    @Test
    void updatePieceSquareId() {
        final int originSquareId = squareId;
        final int newSquareId = chessSquareRepository.save(new Square(File.A, Rank.FOUR, boardId)).getId();
        int affectedRow = chessPieceRepository.updatePieceSquareId(originSquareId, newSquareId);

        assertThat(affectedRow).isEqualTo(1);
    }

    @Test
    void getAllPiecesByBoardId() {
        List<Piece> pieces = chessPieceRepository.getAllPiecesByBoardId(boardId);

        assertThat(pieces.size()).isEqualTo(1);
    }
}
