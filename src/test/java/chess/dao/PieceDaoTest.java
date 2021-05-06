package chess.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
@Sql("classpath:schema.sql")

public class PieceDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private PieceDao pieceDao;

    @BeforeEach
    void setUp() {
        pieceDao = new PieceDao(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @DisplayName("기물 추가 테스트")
    @Test
    void insertInitialPiecesTest() {
        assertDoesNotThrow(() -> pieceDao.insertInitialPieces(1, "p", "d2"));
    }

    @DisplayName("기물 이동 테스트")
    @Test
    void movePieceTest() {
        assertDoesNotThrow(() -> pieceDao.movePiece("e2", "e4", 1));
    }

    @DisplayName("기물 삭제 테스트")
    @Test
    void removePieceTest() {
        assertDoesNotThrow(() -> pieceDao.removePiece("e2", 1));
    }

    @DisplayName("방에 있는 기물들 가져오는 테스트")
    @Test
    void getPiecesTest() {
        assertEquals("p", pieceDao.getPieces(1).get(0).getName());
        assertEquals("e2", pieceDao.getPieces(1).get(0).getPosition());
        assertEquals(1, pieceDao.getPieces(1).size());
    }
}
