package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.game.ChessGame;
import chess.entity.ChessGameEntity;
import chess.entity.ChessGameEntityBuilder;
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
    private static final String SAVED_PASSWORD = "password";
    private long savedId;

    private ChessGameDao chessGameDao;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessGameDao = new ChessGameDao(namedParameterJdbcTemplate);

        jdbcTemplate.execute("drop table board if exists");
        jdbcTemplate.execute("drop table chess_game if exists");
        jdbcTemplate.execute("create table chess_game\n"
                + "(\n"
                + "    id      int primary key auto_increment,\n"
                + "    name               varchar(20) not null unique,\n"
                + "    password           varchar(20) not null,\n"
                + "    power              bool        not null,\n"
                + "    team_value_of_turn varchar(20) not null\n"
                + ")");

        ChessGameEntity newChessGameEntity = new ChessGameEntity(0, SAVED_NAME, SAVED_PASSWORD, true, "WHITE");
        savedId = chessGameDao.save(newChessGameEntity).longValue();
    }

    @Test
    @DisplayName("ChessGameEntity 로 chess_game table 에 저장한다.")
    void save() {
        String TEST_NAME = "jo";
        ChessGame chessGame = ChessGame.createBasic();
        ChessGameEntity savingChessGameEntity = new ChessGameEntityBuilder()
                .setName(TEST_NAME)
                .setPassword(SAVED_PASSWORD)
                .setPower(chessGame.isOn())
                .setTeamValueOfTurn(chessGame.getTurn())
                .build();
        Number savedId = chessGameDao.save(savingChessGameEntity);

        ChessGameEntity chessGameEntity = chessGameDao.load(savedId.longValue());
        assertThat(chessGameEntity.getName()).isEqualTo(TEST_NAME);
    }

    @Test
    @DisplayName("id 를 이용해서 chess_game 를 삭제한다")
    void delete() {
        ChessGameEntity chessGameEntity = new ChessGameEntityBuilder()
                .setId(savedId)
                .setPassword(SAVED_PASSWORD)
                .build();
        chessGameDao.delete(chessGameEntity);

        List<ChessGameEntity> chessGameEntities = chessGameDao.loadAll();
        assertThat(chessGameEntities.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("id 로 chess_game 를 조회한다.")
    void load() {
        ChessGameEntity chessGameEntity = chessGameDao.load(savedId);

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
        ChessGameEntity chessGameEntityForUpdate = new ChessGameEntity(savedId, SAVED_NAME, SAVED_PASSWORD, false,
                "WHITE");
        chessGameDao.updateIsOnAndTurn(chessGameEntityForUpdate);

        ChessGameEntity chessGameEntity = chessGameDao.load(savedId);

        assertAll(
                () -> assertThat(chessGameEntity.getPower()).isFalse(),
                () -> assertThat(chessGameEntity.getTeamValueOfTurn()).isEqualTo("WHITE")
        );
    }
}