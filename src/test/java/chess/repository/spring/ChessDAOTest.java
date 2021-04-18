package chess.repository.spring;

import chess.domain.history.History;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class ChessDAOTest {

    private ChessDAO chessDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        chessDAO = new ChessDAO(jdbcTemplate);
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS ROOM" +
                "(ID   INT NOT NULL AUTO_INCREMENT," +
                "NAME VARCHAR(255)," +
                "PRIMARY KEY (ID)" +
                ");");
        String roomQuery = "INSERT INTO ROOM (NAME) VALUES (?)";
        jdbcTemplate.update(roomQuery, "room1");
        jdbcTemplate.update(roomQuery, "room2");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS History (" +
                "ID INT NOT NULL AUTO_INCREMENT," +
                "SOURCE VARCHAR(255)," +
                "DESTINATION VARCHAR(255)," +
                "TEAM_TYPE VARCHAR(255)," +
                "ROOM_ID INT NOT NULL," +
                "PRIMARY KEY (ID)," +
                "CONSTRAINT ROOM_FK FOREIGN KEY (ROOM_ID) REFERENCES ROOM (ID)" +
                ")");

        String historyQuery = "INSERT INTO HISTORY (SOURCE, DESTINATION, TEAM_TYPE, ROOM_ID) VALUES(?, ?, ?, ?)";
        jdbcTemplate.update(historyQuery, "a1", "a2", "WHITE", 1);
        jdbcTemplate.update(historyQuery, "a6", "b5", "BLACK", 1);
        jdbcTemplate.update(historyQuery, "b1", "b2", "WHITE", 2);
    }

    @DisplayName("DB에 저장된 모든 History들 중 방 번호에 맞는 엔티티들만 조회한다.")
    @Test
    void findAllHistories() {
        List<History> histories = chessDAO.findAllHistoriesByRoomId(1);

        assertThat(histories).hasSize(2);
    }

    @DisplayName("DB에 History를 insert한다.")
    @Test
    void insertHistory() {
        chessDAO.insertHistoryByRoomId("a1", "a3", "WHITE", 2);

        assertThat(chessDAO.findAllHistoriesByRoomId(2)).hasSize(2);
    }

    @DisplayName("DB의 Histories를 전부 delete한다.")
    @Test
    void deleteAllHistories() {
        chessDAO.deleteAllHistoriesByRoomId(2);

        assertThat(chessDAO.findAllHistoriesByRoomId(2)).isEmpty();
    }
}
