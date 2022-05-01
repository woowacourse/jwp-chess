package chess.model.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.entity.PieceEntity;
import chess.model.board.Board;
import chess.model.board.BoardFactory;
import chess.model.piece.PieceFactory;
import chess.model.position.Position;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("/initPieces.sql")
class PieceDaoTest {

    private PieceDao pieceDao;

    @Autowired
    PieceDaoTest(JdbcTemplate jdbcTemplate) {
        pieceDao = new PieceDao(jdbcTemplate);
    }

    @Test
    void savePieces() {
        Board board = BoardFactory.create();
        pieceDao.savePieces(board, 1L);
        List<PieceEntity> rawBoard = pieceDao.findAllPiecesByGameId(1L);

        Board resultBoard = new Board(rawBoard.stream()
                .collect(Collectors.toMap(
                        piece -> Position.from(piece.getPosition()),
                        piece -> PieceFactory.create(piece.getName()))
                ));

        assertThat(resultBoard).isEqualTo(board);
    }

    @Test
    void findNameByPositionAndGameId() {
        pieceDao.savePieces(BoardFactory.create(), 1L);
        String pieceName = pieceDao.findNameByPositionAndGameId("a2", 1L);

        assertThat(pieceName).isEqualTo("white-p");
    }

    @Test
    void updateByPositionAndGameId() {
        pieceDao.savePieces(BoardFactory.create(), 1L);
        pieceDao.updateByPositionAndGameId("black-b", "c3", 1L);
        String pieceName = pieceDao.findNameByPositionAndGameId("c3", 1L);

        assertThat(pieceName).isEqualTo("black-b");
    }

    @Test
    void deleteByGameId() {
        pieceDao.savePieces(BoardFactory.create(), 1L);
        pieceDao.deleteByGameId(1L);

        assertThat(pieceDao.findAllPiecesByGameId(1L)).isEmpty();
    }
}
