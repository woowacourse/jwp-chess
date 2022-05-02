package chess.dao;

import chess.domain.game.BoardEntity;
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

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
class WebChessPositionDaoTest {

    private final PositionDao<Position> positionDao;
    private final BoardDao<BoardEntity> boardDao;
    private final PieceDao<Piece> pieceDao;

    private int boardId;

    @Autowired
    WebChessPositionDaoTest(NamedParameterJdbcTemplate jdbcTemplate) {
        positionDao = new WebChessPositionDao(jdbcTemplate);
        boardDao = new WebChessBoardDao(new WebChessMemberDao(jdbcTemplate), jdbcTemplate);
        pieceDao = new WebChessPieceDao(jdbcTemplate);
    }

    @BeforeEach
    void setup() {
        final BoardEntity board = boardDao.save(new BoardEntity("코린파이팅", "1234"));
        this.boardId = board.getId();
        Position position = positionDao.save(new Position(Column.A, Row.TWO, boardId));
        pieceDao.save(new Piece(Color.WHITE, new Pawn(), position.getId()));
    }

    @Test
    void save() {
        final Position Position = positionDao.save(new Position(Column.B, Row.TWO, boardId));
        assertAll(
                () -> assertThat(Position.getColumn()).isEqualTo(Column.B),
                () -> assertThat(Position.getRow()).isEqualTo(Row.TWO)
        );
    }

    @Test
    void findByColumnAndRowAndBoardId() {
        Position Position = positionDao.findByColumnAndRowAndBoardId(Column.A, Row.TWO, boardId).get();
        assertAll(
                () -> assertThat(Position.getColumn()).isEqualTo(Column.A),
                () -> assertThat(Position.getRow()).isEqualTo(Row.TWO)
        );
    }

    @Test
    void findAllPositionsAndPieces() {
        Map<Position, Piece> all = positionDao.findAllPositionsAndPieces(boardId);

        for (Position position : all.keySet()) {
            assertThat(all.get(position).getType()).isInstanceOf(Pawn.class);
        }
    }

    @Test
    void saveAllPositionTest() {
        final int savedRecords = positionDao.saveAll(boardId);
        assertThat(savedRecords).isEqualTo(64);
    }


}
