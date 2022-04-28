package chess.dao;

import chess.domain.Team;
import chess.dto.request.GameIdRequest;
import chess.dto.request.MakeRoomRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@JdbcTest
public class ChessRoomDaoTest {

    private ChessRoomDao chessRoomDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessRoomDao = new ChessRoomDao(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE room IF EXISTS CASCADE");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS room(" +
                "  id bigint NOT NULL AUTO_INCREMENT,\n" +
                "  status varchar(50) NOT NULL,\n" +
                "  name varchar(50) NOT NULL,\n" +
                "  password varchar(20) NOT NULL,\n" +
                "  PRIMARY KEY (id)" +
                ")");

        jdbcTemplate.update("insert into room (id, status, name, password) values(?, ?, ?, ?)",
                1000, "WHITE", "green", "1234");
    }

    @AfterEach
    void delete() {
        assertThatNoException().isThrownBy(() -> chessRoomDao.deleteGame(1000));
    }

    @Test
    @DisplayName("체스 게임 방 검색 확인")
    void findRoom() {
        assertThat(chessRoomDao.findById(new MakeRoomRequest("green", "1234"))).isNotNull();
    }

    @Test
    @DisplayName("체스 게임 방 저장 확인")
    void saveRoom() {
        assertThatNoException().isThrownBy(() -> chessRoomDao.makeGame(Team.WHITE,
                new MakeRoomRequest("green", "1234")));
    }

    @Test
    @DisplayName("체스 게임 방 상태 업데이트 확인")
    void updateStatus() {
        assertThatNoException().isThrownBy(() -> chessRoomDao.updateStatus(Team.WHITE,
                chessRoomDao.findById(new GameIdRequest(1000L)).getId()));
    }
}
