package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.game.ChessBoard;
import chess.domain.pieces.Color;
import chess.domain.pieces.Pawn;
import chess.domain.pieces.Piece;
import chess.domain.position.Column;
import chess.domain.position.Position;
import chess.domain.position.Row;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class WebPieceDaoTest {

    @Autowired
    private WebChessPositionDao webChessPositionDao;

    @Autowired
    private WebChessPieceDao webPieceDao;

    private final BoardDao<ChessBoard> boardDao = new ChessBoardDao(new ChessConnectionManager());
    private int boardId;
    private int positionId;

    @BeforeEach
    void setup() {
        final ChessBoard board = boardDao.save(new ChessBoard("corinne"));
        this.boardId = board.getId();
        final Position position = webChessPositionDao.save(new Position(Column.A, Row.TWO, board.getId()));
        this.positionId = position.getId();
        final Piece piece = webPieceDao.save(new Piece(Color.WHITE, new Pawn(), positionId));
    }

    @Test
    void saveTest() {
        final Piece piece = webPieceDao.save(new Piece(Color.WHITE, new Pawn(), positionId));
        assertAll(
                () -> assertThat(piece.getType()).isInstanceOf(Pawn.class),
                () -> assertThat(piece.getColor()).isEqualTo(Color.WHITE),
                () -> assertThat(piece.getPositionId()).isEqualTo(positionId)
        );
    }

    @Test
    void findByPositionId() {
        Piece piece = webPieceDao.findByPositionId(positionId).get();
        assertAll(
                () -> assertThat(piece.getType()).isInstanceOf(Pawn.class),
                () -> assertThat(piece.getColor()).isEqualTo(Color.WHITE)
        );
    }

    @Test
    void updatePiecePositionId() {
        final int sourcePositionId = positionId;
        final int targetPositionId = webChessPositionDao.save(new Position(Column.A, Row.TWO, boardId)).getId();
        int affectedRow = webPieceDao.updatePositionId(sourcePositionId, targetPositionId);
        assertThat(affectedRow).isEqualTo(1);
    }

    @Test
    void deletePieceByPositionId() {
        int affectedRows = webPieceDao.deleteByPositionId(positionId);
        assertThat(affectedRows).isEqualTo(1);
    }

    @Test
    void getAllPiecesTest() {
        final List<Piece> pieces = webPieceDao.getAllByBoardId(boardId);
        assertThat(pieces.size()).isEqualTo(1);
    }

    @AfterEach
    void setDown() {
        boardDao.deleteAll();
    }
}
