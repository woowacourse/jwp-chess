package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.pieces.Color;
import chess.domain.pieces.Pawn;
import chess.domain.pieces.Piece;
import chess.domain.position.Column;
import chess.domain.position.Row;
import chess.entities.GameEntity;
import chess.entities.PieceEntity;
import chess.entities.PositionEntity;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class WebPieceDaoTest {

    @Autowired
    private ChessPositionDao chessPositionDao;

    @Autowired
    private ChessPieceDao webPieceDao;

    @Autowired
    private ChessBoardDao boardDao;

    private int boardId;
    private int positionId;

    @BeforeEach
    void setup() {
        final GameEntity board = boardDao.save(new GameEntity("corinne", "1111"));
        this.boardId = board.getId();
        final PositionEntity position = chessPositionDao.save(new PositionEntity(Column.A, Row.TWO, board.getId()));
        this.positionId = position.getId();
        final PieceEntity pieceEntity = webPieceDao.save(new PieceEntity(Color.WHITE, new Pawn(), positionId));
    }

    @Test
    void saveTest() {
        final PieceEntity pieceEntity = webPieceDao.save(new PieceEntity(Color.WHITE, new Pawn(), positionId));
        assertAll(
                () -> assertThat(pieceEntity.getType()).isInstanceOf(Pawn.class),
                () -> assertThat(pieceEntity.getColor()).isEqualTo(Color.WHITE),
                () -> assertThat(pieceEntity.getPositionId()).isEqualTo(positionId)
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
        final int targetPositionId = chessPositionDao.save(new PositionEntity(Column.A, Row.TWO, boardId)).getId();
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

    @Test
    void countPawnsOnSameColumn() {
        int pawnCount = webPieceDao.countPawnsOnSameColumn(boardId, Column.A, Color.WHITE);

        assertThat(pawnCount).isEqualTo(1);
    }

    @AfterEach
    void setDown() {
        boardDao.deleteAll();
    }
}
