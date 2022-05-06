package chess;

import chess.model.entity.PieceEntity;
import chess.model.board.BoardFactory;
import chess.model.dao.GameDao;
import chess.model.dao.PieceDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("classpath:schema.sql")
@JdbcTest
class PieceDaoTest {
    private PieceDao pieceDao;
    private GameDao gameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void initPieceDaoTest() {
        gameDao = new GameDao(jdbcTemplate);
        pieceDao = new PieceDao(jdbcTemplate);
    }

    @AfterEach
    void cleanDB() {
        pieceDao.deleteAll();
    }

    @Test
    @DisplayName("체스판이 db에 저장되었는지 확인한다")
    void init() {
        long gameId = gameDao.initGame("room", "1234");
        pieceDao.init(BoardFactory.create(), gameId);
        List<PieceEntity> boardMap = pieceDao.findAllByGameId(gameId);

        assertThat(boardMap.size()).isEqualTo(64);
    }

    @Test
    @DisplayName("게임 고유 번호에 대한 체스판의 말을 모두 삭제한다.")
    void deleteByGameId() {
        long gameId = gameDao.initGame("room", "1234");
        pieceDao.init(BoardFactory.create(), gameId);

        pieceDao.deleteByGameId(gameId);

        List<PieceEntity> boardMap = pieceDao.findAllPieces();
        assertThat(boardMap.size()).isZero();
    }

    @Test
    @DisplayName("체스판에서 위치와 게임 아이디로 piece의 이름을 찾아온다.")
    void findPieceNameByPositionAndGameId() {
        long gameId = gameDao.initGame("room1", "1234");
        pieceDao.init(BoardFactory.create(), gameId);
        String pieceName = pieceDao.findPieceNameByPositionAndGameId("a2", gameId);

        assertThat(pieceName).isEqualTo("white-p");
    }
}
