package chess;

import chess.entity.PieceEntity;
import chess.model.board.BoardFactory;
import chess.model.dao.PieceDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class PieceDaoTest {
    static PieceDao pieceDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void initPieceDaoTest() {
        jdbcTemplate.execute("DROP TABLE PIECES IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE pieces\n" +
                "(\n" +
                "    piece_id int         not null AUTO_INCREMENT,\n" +
                "    position varchar(4)  not null,\n" +
                "    name    varchar(10) not null,\n" +
                "    primary key (piece_id)\n" +
                ");");

        pieceDao = new PieceDao(jdbcTemplate);
        pieceDao.init(BoardFactory.create());
    }

    @AfterEach
    void cleanDB() {
        pieceDao.deleteAll();
    }

    @Test
    @DisplayName("체스판이 db에 저장되었는지 확인한다")
    void init() {
        List<PieceEntity> boardMap = pieceDao.findAllPieces();

        assertThat(boardMap.size()).isEqualTo(64);
    }

    @Test
    @DisplayName("체스판이 db에 저장되었는지 확인한다")
    void findByPosition() {
        String pieceName = pieceDao.findPieceNameByPosition("a2");

        assertThat(pieceName).isEqualTo("white-p");
    }

    @Test
    @DisplayName("체스판의 말을 update하는 것을 확인한다.")
    void updatePieceNameByPosition() {
        pieceDao.updateByPosition("a2", "none-.");

        String pieceName = pieceDao.findPieceNameByPosition("a2");

        assertThat(pieceName).isEqualTo("none-.");
    }

    @Test
    @DisplayName("체스판의 말을 모두 삭제한다.")
    void deleteAll() {
        pieceDao.deleteAll();

        List<PieceEntity> boardMap = pieceDao.findAllPieces();
        assertThat(boardMap.size()).isZero();
    }
}
