package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.Game;
import chess.domain.game.ChessBoard;
import chess.domain.game.ChessBoardInitializer;
import chess.domain.pieces.Color;
import chess.domain.pieces.Pawn;
import chess.domain.pieces.Piece;
import chess.domain.position.Column;
import chess.domain.position.Position;
import chess.domain.position.Row;
import chess.entities.GameEntity;
import chess.entities.PieceEntity;
import chess.entities.PositionEntity;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class WebPositionEntityDaoTest {

    @Autowired
    private ChessPositionDao chessPositionDao;

    @Autowired
    private ChessBoardDao boardDao;

    @Autowired
    private ChessPieceDao pieceDao;

    private int boardId;
    private int positionId;

    @BeforeEach
    void setup() {
        final GameEntity board = boardDao.save(new GameEntity("코린파이팅", "1111", new Game(new ChessBoard(new ChessBoardInitializer()), Color.WHITE)));
        this.boardId = board.getId();
        PositionEntity positionEntity = chessPositionDao.save(new PositionEntity(Column.A, Row.TWO, boardId));
        this.positionId = positionEntity.getId();
        pieceDao.save(new PieceEntity(Color.WHITE, new Pawn(), positionEntity.getId()));
    }

    @Test
    void save() {
        final PositionEntity positionEntity = chessPositionDao.save(new PositionEntity(Column.B, Row.TWO, boardId));
        assertAll(
                () -> assertThat(positionEntity.getColumn()).isEqualTo(Column.B),
                () -> assertThat(positionEntity.getRow()).isEqualTo(Row.TWO)
        );
    }

    @Test
    void findByColumnAndRowAndBoardId() {
        PositionEntity positionEntity = chessPositionDao.getByColumnAndRowAndBoardId(Column.A, Row.TWO, boardId);
        assertAll(
                () -> assertThat(positionEntity.getColumn()).isEqualTo(Column.A),
                () -> assertThat(positionEntity.getRow()).isEqualTo(Row.TWO)
        );
    }

    @Test
    void findAllPositionsAndPieces() {
        Map<Position, Piece> all = chessPositionDao.findAllPositionsAndPieces(boardId);

        for (Position position : all.keySet()) {
            assertThat(all.get(position).getType()).isInstanceOf(Pawn.class);
        }
    }

    @Test
    void saveAllPositionTest() {
        final int savedRecords = chessPositionDao.saveAll(boardId);
        assertThat(savedRecords).isEqualTo(64);
    }

    @Test
    void getIdByColumnAndRowAndBoardId() {
        int positionId = chessPositionDao.getIdByColumnAndRowAndBoardId(Column.A, Row.TWO, boardId);

        assertThat(positionId).isEqualTo(this.positionId);
    }

    @Test
    void getPaths() {
        List<PositionEntity> positions = List.of(new PositionEntity(Column.A, Row.TWO, boardId));
        List<PositionEntity> paths = chessPositionDao.getPaths(positions, boardId);

        assertThat(paths.get(0).getId()).isEqualTo(positionId);
    }

    @AfterEach
    void setDown() {
        boardDao.deleteAll();
    }
}
