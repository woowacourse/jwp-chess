package chess.spring.dao;

import chess.domain.history.History;
import chess.repository.spring.ChessDAO;
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

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS History (" +
                "ID INT NOT NULL AUTO_INCREMENT," +
                "SOURCE VARCHAR(255)," +
                "DESTINATION VARCHAR(255)," +
                "TEAM_TYPE VARCHAR(255)," +
                "PRIMARY KEY (ID)" +
                ")");
        String query = "INSERT INTO HISTORY (SOURCE, DESTINATION, TEAM_TYPE) VALUES(?, ?, ?)";
        jdbcTemplate.update(query, "a1", "a2", "WHITE");
        jdbcTemplate.update(query, "a6", "b5", "BLACK");
    }

    @DisplayName("DB에 저장된 모든 History들을 조회한다.")
    @Test
    void findAllHistories() {
        List<History> histories = chessDAO.findAllHistories();

        assertThat(histories).hasSize(2);
    }

    @DisplayName("DB에 History를 insert한다.")
    @Test
    void insertHistory() {
        chessDAO.insertHistory("a1", "a3", "WHITE");

        assertThat(chessDAO.findAllHistories()).hasSize(3);
    }

    @DisplayName("DB의 Histories를 전부 delete한다.")
    @Test
    void deleteAllHistories() {
        chessDAO.deleteAllHistories();

        assertThat(chessDAO.findAllHistories()).hasSize(0);
    }
}
