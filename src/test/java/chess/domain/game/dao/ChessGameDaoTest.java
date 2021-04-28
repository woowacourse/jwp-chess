package chess.domain.game.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.game.ChessGame;
import chess.domain.game.dto.ChessGameDto;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
class ChessGameDaoTest {

    private ChessGameDao chessGameDao;

    private final List<String> chessNames = Arrays.asList("Chess1", "Chess2");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessGameDao = new ChessGameDao(jdbcTemplate);

        jdbcTemplate.batchUpdate(
                "INSERT INTO chess_game (name) VALUES (?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, chessNames.get(i));
                    }

                    @Override
                    public int getBatchSize() {
                        return chessNames.size();
                    }
                });
    }

    @Test
    void addGame() {
        ChessGame chessGame = ChessGame.initChessGame("new game");

        Long gameId = chessGameDao.addGame(chessGame);

        assertThat(gameId).isNotNull();
        assertThat(gameId).isNotEqualTo(0L);
    }

    @Test
    void findActiveGames() {
        List<ChessGameDto> activeGames = chessGameDao.findActiveGames();

        assertThat(activeGames).size().isEqualTo(chessNames.size());
        for (int i = 0; i < activeGames.size(); i++) {
            String gameResultName = activeGames.get(i).getName();
            assertThat(gameResultName).isEqualTo(chessNames.get(i));
        }
    }

    @Test
    void updateGameEnd() {
        String idSql = "SELECT id FROM chess_game LIMIT 1";
        Integer id = jdbcTemplate.queryForObject(idSql, Integer.class);

        chessGameDao.updateGameEnd(String.valueOf(id));

        String sql = "SELECT is_end FROM chess_game WHERE id = ?";
        assertThat(jdbcTemplate.queryForObject(sql, Boolean.class, id)).isTrue();
    }

    @Test
    void findGameById() {
        String idSql = "SELECT id FROM chess_game LIMIT 1";
        Integer id = jdbcTemplate.queryForObject(idSql, Integer.class);

        ChessGameDto chessGameDto = chessGameDao.findGameById(String.valueOf(id));

        assertThat(chessGameDto.getId()).isEqualTo(String.valueOf(id));
        assertThat(chessGameDto.getName()).isEqualTo(chessNames.get(0));
    }
}