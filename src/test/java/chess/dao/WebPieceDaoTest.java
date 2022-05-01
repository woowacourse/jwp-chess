package chess.dao;

import chess.domain.game.ChessBoard;
import chess.domain.pieces.Blank;
import chess.domain.pieces.Color;
import chess.domain.pieces.Pawn;
import chess.domain.pieces.Piece;
import chess.domain.position.Column;
import chess.domain.position.Position;
import chess.domain.position.Row;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Sql({"schema.sql"})
class WebPieceDaoTest {

    private final PositionDao<Position> positionDao;
    private final PieceDao<Piece> pieceDao;
    private final BoardDao<ChessBoard> boardDao;

    private int boardId;
    private int positionId;

    @Autowired
    WebPieceDaoTest(NamedParameterJdbcTemplate jdbcTemplate) {
        positionDao = new WebChessPositionDao(jdbcTemplate);
        pieceDao = new WebChessPieceDao(jdbcTemplate);
        boardDao = new WebChessBoardDao(new WebChessMemberDao(jdbcTemplate), jdbcTemplate);
    }

    @BeforeEach
    void setup() {
        final ChessBoard board = boardDao.save(new ChessBoard("corinne", "1234"));
        this.boardId = board.getId();
        final Position position = positionDao.save(new Position(Column.A, Row.TWO, board.getId()));
        this.positionId = position.getId();
        pieceDao.save(new Piece(Color.WHITE, new Pawn(), positionId));
    }

    @Test
    void saveTest() {
        final Piece piece = pieceDao.save(new Piece(Color.WHITE, new Pawn(), positionId));
        assertAll(
                () -> assertThat(piece.getType()).isInstanceOf(Pawn.class),
                () -> assertThat(piece.getColor()).isEqualTo(Color.WHITE),
                () -> assertThat(piece.getPositionId()).isEqualTo(positionId)
        );
    }

    @Test
    void findByPositionId() {
        Piece piece = pieceDao.findByPositionId(positionId).get();
        assertAll(
                () -> assertThat(piece.getType()).isInstanceOf(Pawn.class),
                () -> assertThat(piece.getColor()).isEqualTo(Color.WHITE)
        );
    }

    @Test
    void updatePiece() {
        Piece sourcePiece = pieceDao.findByPositionId(positionId).get();
        int affectedRow = pieceDao.updatePiece(sourcePiece, new Piece(Color.NONE, new Blank()));
        assertThat(affectedRow).isEqualTo(1);
    }

    @Test
    void deletePieceByPositionId() {
        int affectedRows = pieceDao.deleteByPositionId(positionId);
        assertThat(affectedRows).isEqualTo(1);
    }

    @Test
    void getAllPiecesTest() {
        final List<Piece> pieces = pieceDao.getAllByBoardId(boardId);
        assertThat(pieces.size()).isEqualTo(1);
    }
}
