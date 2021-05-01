package chess.mysql.dao;

import chess.mysql.chess.ChessConnection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class ChessConnectionTest {
    @DisplayName("DB 연결 확인")
    @Test
    void getConnection() throws SQLException {
        //given
        //when
        Connection connection = ChessConnection.getConnection();
        //then
        assertThat(connection.isClosed()).isFalse();
    }
}