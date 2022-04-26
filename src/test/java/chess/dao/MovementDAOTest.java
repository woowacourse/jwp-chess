package chess.dao;

import static org.assertj.core.api.Assertions.*;

import chess.dto.GameCreationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MovementDAOTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void before() {
        ChessGameDAO chessGameDAO = new ChessGameDAO(jdbcTemplate);
        long id = chessGameDAO.addGame(new GameCreationDTO("zero", "1234"));

        assertThat(id).isNotNull();
    }
}
