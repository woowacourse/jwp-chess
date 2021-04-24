package chess.domain.dao;

import chess.domain.dto.CommandDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class SparkCommandDaoTest {
    private SparkCommandDao sparkCommandDao;

    @BeforeEach
    public void setup() {
        sparkCommandDao = new SparkCommandDao();
    }

    @Test
    public void addUser() throws Exception {
        CommandDto commandDto = new CommandDto("start");
        sparkCommandDao.insert(commandDto, 1);
    }
}

