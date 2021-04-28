package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import chess.domain.ChessGame;
import chess.domain.team.BlackTeam;
import chess.domain.team.Team;
import chess.domain.team.WhiteTeam;
import java.sql.PreparedStatement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Sql("classpath:tableInit.sql")
class TeamDaoTest {
    private final TeamDao teamDao;
    private final JdbcTemplate jdbcTemplate;

    public TeamDaoTest(final TeamDao teamDao, final JdbcTemplate jdbcTemplate) {
        this.teamDao = teamDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        final ChessGame chessGame = new ChessGame(new WhiteTeam(), new BlackTeam());
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con
                .prepareStatement("insert into game (is_end) values (?)");
            preparedStatement.setBoolean(1, chessGame.isEnd());
            return preparedStatement;
        });

        final Team blackTeam = chessGame.getBlackTeam();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con
                .prepareStatement("insert into team (name, is_turn, game_id) values (?, ?, ?)");
            preparedStatement.setString(1, blackTeam.getName());
            preparedStatement.setBoolean(2, blackTeam.isCurrentTurn());
            preparedStatement.setLong(3, 1);
            return preparedStatement;
        });

        final Team whiteTeam = chessGame.getWhiteTeam();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con
                .prepareStatement("insert into team (name, is_turn, game_id) values (?, ?, ?)");
            preparedStatement.setString(1, whiteTeam.getName());
            preparedStatement.setBoolean(2, whiteTeam.isCurrentTurn());
            preparedStatement.setLong(3, 1);
            return preparedStatement;
        });
    }

    @Test
    @DisplayName("팀 생성 테스트")
    void create() {
        final ChessGame chessGame = new ChessGame(new WhiteTeam(), new BlackTeam());
        assertThatCode(() -> teamDao.create(chessGame.getBlackTeam(), 1))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("팀정보 가져오기 테스트")
    void load() {
        final Team whiteTeam = teamDao.load(1, "White");
        assertThat(whiteTeam.getName()).isEqualTo("White");
        assertThat(whiteTeam.isCurrentTurn()).isTrue();

        final Team blackTeam = teamDao.load(1, "Black");
        assertThat(blackTeam.getName()).isEqualTo("Black");
        assertThat(blackTeam.isCurrentTurn()).isFalse();
    }

    @Test
    @DisplayName("팀 정보 수정 테스트")
    void update() {
        Team whiteTeam = teamDao.load(1, "White");
        whiteTeam.endTurn();
        assertThatCode(() -> teamDao.update(1, whiteTeam)).doesNotThrowAnyException();
        assertThat(teamDao.load(1, "White").isCurrentTurn()).isFalse();

        Team blackTurn = teamDao.load(1, "Black");
        blackTurn.startTurn();
        assertThatCode(() -> teamDao.update(1, blackTurn)).doesNotThrowAnyException();
        assertThat(teamDao.load(1, "Black").isCurrentTurn()).isTrue();
    }
}