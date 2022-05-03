package chess.dao;

import chess.domain.Team;
import chess.dto.request.GameIdRequest;
import chess.dto.request.RoomRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
public class ChessRoomDaoTest {

    private ChessRoomDao chessRoomDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessRoomDao = new ChessRoomDao(jdbcTemplate);
        jdbcTemplate.update("insert into room(id, status, name, password) values(?, ?, ?, ?)",
                1000, "WHITE", "green", "1234");
    }

    @Test
    @DisplayName("체스 게임 방 검색 확인")
    void findRoom() {
        assertThat(chessRoomDao.findById(new RoomRequest("green", "1234")).getId())
                .isEqualTo(1000);
    }

    @Test
    @DisplayName("체스 게임 방 저장 확인")
    void saveRoom() {
        chessRoomDao.makeGame(Team.WHITE, new RoomRequest("greenn", "12345"));

        assertThat(chessRoomDao.findById(new RoomRequest("greenn", "12345")))
                .isNotNull();
    }

    @Test
    @DisplayName("체스 게임 방 상태 업데이트 확인")
    void updateStatus() {
        chessRoomDao.updateStatus(Team.WHITE, chessRoomDao.findById(new GameIdRequest(1000L)).getId());

        assertThat(chessRoomDao.findById(new GameIdRequest(1000L)).getStatus()).isEqualTo(Team.WHITE);
    }

    @Test
    @DisplayName("체스 게임 방 삭제 확인")
    void deleteGame() {
        chessRoomDao.deleteGame(1000);

        assertThatThrownBy(() -> chessRoomDao.findById(new GameIdRequest(1000L)))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
