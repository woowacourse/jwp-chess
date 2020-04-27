package wooteco.chess.repository;

import wooteco.chess.util.JDBCConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTemplate {

    public void executeUpdate(String query, PreparedStatementSetter pss) throws SQLException {
        try (Connection con = JDBCConnector.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)
        ) {
            pss.setParameters(pstmt);
            pstmt.executeUpdate();
        }
    }

    public void executeBatch(String query, PreparedStatementSetter pss) throws SQLException {
        try (Connection con = JDBCConnector.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)
        ) {
            pss.setParameters(pstmt);
            pstmt.executeBatch();
        }
    }

    public <T> T executeQuery(String query, PreparedStatementSetter pss, RowMapper<T> rm) throws SQLException {
        try (Connection con = JDBCConnector.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)
        ) {
            pss.setParameters(pstmt);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rm.mapRow(rs);
            }
        }
    }
}
