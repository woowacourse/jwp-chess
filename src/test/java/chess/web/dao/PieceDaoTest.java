package chess.web.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.Color;
import chess.domain.piece.King;
import chess.domain.piece.Queen;
import chess.domain.position.Position;
import chess.domain.state.StateType;
import chess.web.dto.PieceDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;


@JdbcTest
@Sql("classpath:init.sql")
class PieceDaoTest {

    private static final int GAME_ID = 1;

    private PieceDao pieceDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        GameDao gameDao = new GameDaoJdbcImpl(jdbcTemplate);
        pieceDao = new PieceDaoJdbcImpl(jdbcTemplate);

        gameDao.save("체스게임방", "1234", StateType.WHITE_TURN);
        pieceDao.save(GAME_ID, new PieceDto(new King(Color.WHITE), new Position("a1")));
    }

    @DisplayName("기물 정보를 변경한다.")
    @Test
    void updateByGameId() {
        pieceDao.updateByGameId(GAME_ID, new PieceDto(new Queen(Color.WHITE), new Position("a1")));

        List<PieceDto> pieceDtos = pieceDao.findAllByGameId(GAME_ID);
        String actual = pieceDtos.get(0).getPieceType();
        String expected = "Q";

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("특정 게임의 모든 기물을 선택한다.")
    @Test
    void findAllByGameId() {
        List<PieceDto> pieceDtos = pieceDao.findAllByGameId(GAME_ID);

        int actual = pieceDtos.size();
        int expected = 1;

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("특정 게임의 기물을 전부 삭제한다.")
    @Test
    void deleteAllByGameId() {
        pieceDao.deleteAllByGameId(GAME_ID);

        int actual = pieceDao.findAllByGameId(GAME_ID).size();
        int expected = 0;

        assertThat(actual).isEqualTo(expected);
    }
}
