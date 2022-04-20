package springchess.dao;

import chess.dao.ChessBoardDao;
import chess.dao.ChessPieceDao;
import chess.dao.ChessSquareDao;
import chess.dao.ConnectionManager;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ChessPieceDaoTest {

    private final chess.dao.ChessPieceDao dao = new ChessPieceDao(new chess.dao.ConnectionManager());
    private final chess.dao.ChessSquareDao chessSquareDao = new ChessSquareDao(new chess.dao.ConnectionManager());
    private final chess.dao.ChessBoardDao chessBoardDao = new ChessBoardDao(new ConnectionManager());
    private int boardId;
    private int squareId;

    @BeforeEach
    void setup() {
        final Board board = chessBoardDao.save(new Board(new Running(), Team.WHITE));
        this.boardId = board.getId();
        final Square square = chessSquareDao.save(new Square(File.A, Rank.TWO, board.getId()));
        this.squareId = square.getId();
        dao.save(new Pawn(Team.WHITE), squareId);
    }

    @AfterEach
    void setDown() {
        chessBoardDao.deleteAll();
    }

    @Test
    void saveTest() {
        final Piece piece = dao.save(new Pawn(Team.WHITE), squareId);

        assertAll(
                () -> assertThat(piece.name()).isEqualTo("p"),
                () -> assertThat(piece.team()).isEqualTo(Team.WHITE),
                () -> assertThat(piece.getSquareId()).isEqualTo(squareId)
        );
    }

    @Test
    void findBySquareId() {
        Piece piece = dao.findBySquareId(squareId);

        assertAll(
                () -> assertThat(piece.name()).isEqualTo("p"),
                () -> assertThat(piece.team()).isEqualTo(Team.WHITE)
        );
    }

    @Test
    void deletePieceBySquareId() {
        int affectedRows = dao.deletePieceBySquareId(squareId);

        assertThat(affectedRows).isEqualTo(1);
    }

    @Test
    void updatePieceSquareId() {
        final int originSquareId = squareId;
        final int newSquareId = chessSquareDao.save(new Square(File.A, Rank.FOUR, boardId)).getId();
        int affectedRow = dao.updatePieceSquareId(originSquareId, newSquareId);

        assertThat(affectedRow).isEqualTo(1);
    }

    @Test
    void getAllPiecesByBoardId() {
        List<Piece> pieces = dao.getAllPiecesByBoardId(boardId);

        assertThat(pieces.size()).isEqualTo(1);
    }
}
