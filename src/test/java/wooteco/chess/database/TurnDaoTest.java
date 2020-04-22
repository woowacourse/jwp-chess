package wooteco.chess.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wooteco.chess.database.dao.TurnDao;
import wooteco.chess.domain.piece.Team;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class TurnDaoTest {
    private TurnDao turnDao;
    private Team savedTurn;

    @BeforeEach
    public void setUp() throws SQLException {
        turnDao = new TurnDao();
        savedTurn = turnDao.load();
    }

    @Test
    void saveTurnTest() throws SQLException {
        turnDao.save(Team.WHITE);
        assertThat(turnDao.load()).isEqualTo(Team.WHITE);
        turnDao.save(Team.BLACK);
        assertThat(turnDao.load()).isEqualTo(Team.BLACK);
    }

    @AfterEach
    void tearDown() throws SQLException {
        turnDao.save(savedTurn);
    }
}
