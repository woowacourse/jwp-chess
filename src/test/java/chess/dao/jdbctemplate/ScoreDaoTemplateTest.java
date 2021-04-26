package chess.dao.jdbctemplate;

import chess.dao.GameDao;
import chess.dao.ScoreDao;
import chess.dao.dto.game.GameDto;
import chess.dao.dto.score.ScoreDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@DataJdbcTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestPropertySource("classpath:application-test.properties")
class ScoreDaoTemplateTest {

    private JdbcTemplate jdbcTemplate;
    private ScoreDao scoreDao;
    private GameDao gameDao;

    public ScoreDaoTemplateTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.scoreDao = new ScoreDaoTemplate(jdbcTemplate);
        this.gameDao = new GameDaoTemplate(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        Long newGameId = gameDao.save(new GameDto("게임", "흰색유저", "흑색유저"));
        String sql = "INSERT INTO score(game_id, white_score, black_score) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, newGameId, 38.0, 38.0);
    }

    @Test
    void saveScore() {
        //given
        Long newGameId = gameDao.save(new GameDto("게임", "흰색유저", "흑색유저"));
        double whiteScore = 37.0d;
        double blackScore = 37.0d;
        ScoreDto scoreDto = new ScoreDto(newGameId, whiteScore, blackScore);

        //when
        scoreDao.save(scoreDto);
        ScoreDto findScore = scoreDao.findByGameId(newGameId);

        //then
        assertThat(findScore).isNotNull();
        assertThat(findScore.getWhiteScore()).isEqualTo(whiteScore);
        assertThat(findScore.getBlackScore()).isEqualTo(blackScore);
    }

    @Test
    void updateScore() {
        //given
        Long newGameId = gameDao.save(new GameDto("게임", "흰색유저", "흑색유저"));
        double whiteScore = 37.0d;
        double blackScore = 37.0d;
        ScoreDto scoreDto = new ScoreDto(newGameId, whiteScore, blackScore);
        scoreDao.save(scoreDto);
        double updatedWhiteScore = 35.0d;
        double updatedBlackScore = 36.0d;
        ScoreDto updateScoreDto = new ScoreDto(newGameId, updatedWhiteScore, updatedBlackScore);

        //when
        scoreDao.update(updateScoreDto);
        ScoreDto updatedScore = scoreDao.findByGameId(newGameId);

        //then
        assertThat(updatedScore).isNotNull();
        assertThat(updatedScore.getWhiteScore()).isEqualTo(updatedWhiteScore);
        assertThat(updatedScore.getBlackScore()).isEqualTo(updatedBlackScore);
    }

    @Test
    void findScoreByGameId() {
        //given
        Long newGameId = gameDao.save(new GameDto("게임", "흰색유저", "흑색유저"));
        double whiteScore = 37.0d;
        double blackScore = 37.0d;
        ScoreDto scoreDto = new ScoreDto(newGameId, whiteScore, blackScore);
        scoreDao.save(scoreDto);

        //when
        ScoreDto findScore = scoreDao.findByGameId(newGameId);

        //then
        assertThat(findScore).isNotNull();
        assertThat(findScore.getWhiteScore()).isEqualTo(whiteScore);
        assertThat(findScore.getBlackScore()).isEqualTo(blackScore);
    }
}