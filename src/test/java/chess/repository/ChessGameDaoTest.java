package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.repository.entity.ChessGameEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("/settingForTest.sql")
class ChessGameDaoTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private ChessGameDao chessGameDao;

    @BeforeEach
    void setUp() {
        chessGameDao = new ChessGameDao(namedParameterJdbcTemplate);
    }

    @Test
    @DisplayName("ChessGameEntity 로 chess_game table 에 저장한다.")
    void save() {
        ChessGameEntity newChessGameEntity = new ChessGameEntity("2222", true, "WHITE");

        chessGameDao.save(newChessGameEntity);

        ChessGameEntity loadedChessGameEntity = chessGameDao.load("2222");
        assertAll(
                () -> assertThat(loadedChessGameEntity.getIsOn()).isEqualTo(true),
                () -> assertThat(loadedChessGameEntity.getTeamValueOfTurn()).isEqualTo("WHITE")
        );
    }

    @Test
    @DisplayName("gameRoomId 로 chess_game table 데이터를 조회한다.")
    void load() {
        ChessGameEntity loadedChessGameEntity = chessGameDao.load("1111");

        assertAll(
                () -> assertThat(loadedChessGameEntity.getIsOn()).isEqualTo(true),
                () -> assertThat(loadedChessGameEntity.getTeamValueOfTurn()).isEqualTo("BLACK")
        );
    }

    @Test
    @DisplayName("ChessGameEntity 를 이용해서 chess_game table 데이터를 업데이트 한다.")
    void updatePiece() {
        ChessGameEntity newChessGameEntity = new ChessGameEntity("1111", false, "WHITE");

        chessGameDao.updateChessGame(newChessGameEntity);

        ChessGameEntity loadedChessGameEntity = chessGameDao.load("1111");
        assertAll(
                () -> assertThat(loadedChessGameEntity.getIsOn()).isEqualTo(false),
                () -> assertThat(loadedChessGameEntity.getTeamValueOfTurn()).isEqualTo("WHITE")
        );
    }
}