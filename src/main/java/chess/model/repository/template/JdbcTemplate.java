package chess.model.repository.template;

import static chess.model.repository.connector.ChessMySqlConnector.getConnection;

import chess.model.repository.exception.DataAccessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcTemplate {

    public static PreparedStatementSetter getPssFromParams(Object... params) {
        return pstmt -> {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
        };
    }

    public static String makeQuery(String... params) {
        StringBuilder query = new StringBuilder();
        for (String param : params) {
            query.append(param);
            query.append(" ");
        }
        return query.toString();
    }

    public void executeUpdate(String query, PreparedStatementSetter pss) {
        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            pss.setParameter(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void executeUpdateWhenLoop(String query, PreparedStatementSetter loopPss) {
        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            loopPss.setParameter(pstmt);
            pstmt.executeBatch();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public int executeUpdateWithGeneratedKey(String query, PreparedStatementSetter pss) {
        try (Connection conn = getConnection();
            PreparedStatement pstmt =
                conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pss.setParameter(pstmt);
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                throw new DataAccessException();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public <T> T executeQuery(String query, PreparedStatementSetter pss,
        ResultSetMapper<T> mapper) {
        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            pss.setParameter(pstmt);
            try (ResultSet rs = pstmt.executeQuery()) {
                return mapper.setRow(rs);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
