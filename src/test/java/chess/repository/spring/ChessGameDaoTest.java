package chess.repository.spring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.repository.entity.ChessGameEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@JdbcTest
class ChessGameDaoTest {

    private static final String SAVED_NAME = "test";

    private ChessGameDao chessGameDao;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessGameDao = new ChessGameDao(namedParameterJdbcTemplate);

        jdbcTemplate.execute("drop table chess_game if exists");
        jdbcTemplate.execute("create table chess_game\n"
                + "(\n"
                + "    chess_game_id      int primary key auto_increment,\n"
                + "    name               varchar(20) not null unique,\n"
                + "    is_on              bool        not null,\n"
                + "    team_value_of_turn varchar(20) not null\n"
                + ")");

        jdbcTemplate.execute(
                "insert into chess_game (name, is_on, team_value_of_turn)"
                        + " values ('" + SAVED_NAME + "', true, 'BLACK')");
    }

    @Test
    @DisplayName("ChessGameEntity 로 chess_game table 에 저장한다.")
    void save() {
        String TEST_NAME = "jo";
        ChessGameEntity newChessGameEntity = new ChessGameEntity(TEST_NAME, true, "WHITE");

        chessGameDao.save(newChessGameEntity);

        ChessGameEntity chessGameEntity = chessGameDao.load(TEST_NAME);
        assertThat(chessGameEntity.getName()).isEqualTo(TEST_NAME);
    }

    @Test
    @DisplayName("name 을 이용해서 chess_game 를 삭제한다")
    void delete() {
        chessGameDao.delete(SAVED_NAME);

        List<ChessGameEntity> chessGameEntities = chessGameDao.loadAll();
        assertThat(chessGameEntities.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("name 으로 chess_game 를 조회한다.")
    void load() {
        ChessGameEntity chessGameEntity = chessGameDao.load(SAVED_NAME);

        assertThat(chessGameEntity.getName()).isEqualTo(SAVED_NAME);
    }

    @Test
    @DisplayName("모든 chess_game 을 조회한다.")
    void loadAll() {
        List<ChessGameEntity> chessGameEntities = chessGameDao.loadAll();

        assertThat(chessGameEntities.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("chessGameEntity 를 이용해서 chess_game 을 업데이트 한다.")
    void updatePiece() {
        ChessGameEntity chessGameEntityForUpdate = new ChessGameEntity(SAVED_NAME, false, "WHITE");
        chessGameDao.updateIsOnAndTurn(chessGameEntityForUpdate);

        ChessGameEntity chessGameEntity = chessGameDao.load(SAVED_NAME);

        assertAll(
                () -> assertThat(chessGameEntity.getIsOn()).isFalse(),
                () -> assertThat(chessGameEntity.getTeamValueOfTurn()).isEqualTo("WHITE")
        );
    }
}