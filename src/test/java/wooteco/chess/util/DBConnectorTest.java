package wooteco.chess.util;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DBConnectorTest {

    private DBConnector dbConnector;

    @BeforeEach
    void setUp() {
        dbConnector = DBConnector.getInstance();
    }

    @Test
    public void connection() {
        Connection con = dbConnector.getConnection();
        assertNotNull(con);
    }
}