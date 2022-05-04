package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import chess.repository.dao.GameRoomDao;
import chess.repository.entity.GameRoomEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("/settingForTest.sql")
class GameRoomDaoTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private GameRoomDao gameRoomDao;

    @BeforeEach
    void setUp() {
        gameRoomDao = new GameRoomDao(namedParameterJdbcTemplate);
    }

    @Test
    @DisplayName("GameRoomEntity 로 game_room table 에 저장한다.")
    void save() {
        GameRoomEntity newGameRoomEntity = new GameRoomEntity("9999", "save", "0000");

        gameRoomDao.save(newGameRoomEntity);

        GameRoomEntity loadedGameRoomEntity = gameRoomDao.load("9999");
        assertAll(
                () -> assertThat(loadedGameRoomEntity.getName()).isEqualTo("save"),
                () -> assertThat(loadedGameRoomEntity.getPassword()).isEqualTo("0000"),
                () -> assertThat(gameRoomDao.loadAll().size()).isEqualTo(3)
        );
    }

    @Test
    @DisplayName("gameRoomId 와 password 로 game_room table 데이터를 삭제한다.")
    void delete() {
        gameRoomDao.delete("1111", "1111");

        assertThat(gameRoomDao.loadAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("gameRoomId 로 game_room table 데이터를 조회한다.")
    void load() {
        GameRoomEntity loadedGameRoomEntity = gameRoomDao.load("2222");

        assertAll(
                () -> assertThat(loadedGameRoomEntity.getName()).isEqualTo("game2"),
                () -> assertThat(loadedGameRoomEntity.getPassword()).isEqualTo("2222")
        );
    }

    @Test
    @DisplayName("모든 game_room table 데이터를 조회한다.")
    void loadAll() {
        assertThat(gameRoomDao.loadAll().size()).isEqualTo(2);
    }
}