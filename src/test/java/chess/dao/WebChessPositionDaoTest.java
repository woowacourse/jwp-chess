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
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class WebChessPositionDaoTest {

    @Autowired
    private WebChessPositionDao webChessPositionDao;

    @Autowired
    private WebChessBoardDao boardDao;

    @Autowired
    private WebChessPieceDao pieceDao;

    private int boardId;
    private int positionId;

    @BeforeEach
    void setup() {
        final ChessBoard board = boardDao.save(new ChessBoard("코린파이팅"));
        this.boardId = board.getId();
        Position position = webChessPositionDao.save(new Position(Column.A, Row.TWO, boardId));
        this.positionId = position.getId();
        pieceDao.save(new Piece(Color.WHITE, new Pawn(), position.getId()));
    }

    @Test
    void save() {
        final Position Position = webChessPositionDao.save(new Position(Column.B, Row.TWO, boardId));
        assertAll(
                () -> assertThat(Position.getColumn()).isEqualTo(Column.B),
                () -> assertThat(Position.getRow()).isEqualTo(Row.TWO)
        );
    }

    @Test
    void findByColumnAndRowAndBoardId() {
        Position Position = webChessPositionDao.getByColumnAndRowAndBoardId(Column.A, Row.TWO, boardId);
        assertAll(
                () -> assertThat(Position.getColumn()).isEqualTo(Column.A),
                () -> assertThat(Position.getRow()).isEqualTo(Row.TWO)
        );
    }

    @Test
    void findAllPositionsAndPieces() {
        Map<Position, Piece> all = webChessPositionDao.findAllPositionsAndPieces(boardId);

        for (Position position : all.keySet()) {
            assertThat(all.get(position).getType()).isInstanceOf(Pawn.class);
        }
    }

    @Test
    void saveAllPositionTest() {
        final int savedRecords = webChessPositionDao.saveAll(boardId);
        assertThat(savedRecords).isEqualTo(64);
    }

    @Test
    void getIdByColumnAndRowAndBoardId() {
        int positionId = webChessPositionDao.getIdByColumnAndRowAndBoardId(Column.A, Row.TWO, boardId);

        assertThat(positionId).isEqualTo(this.positionId);
    }

    @Test
    void getPaths() {
        List<Position> positions = List.of(new Position(Column.A, Row.TWO, boardId));
        List<Position> paths = webChessPositionDao.getPaths(positions, boardId);

        assertThat(paths.get(0).getId()).isEqualTo(positionId);
    }

    @AfterEach
    void setDown() {
        boardDao.deleteAll();
    }
}
