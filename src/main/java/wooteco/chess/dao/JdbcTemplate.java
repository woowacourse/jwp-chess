package wooteco.chess.dao;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class JdbcTemplate {
    private Connectable connectable;

    public JdbcTemplate(Connectable connectable) {
        this.connectable = connectable;
    }

    public void executeUpdate(String query, PreparedStatementSetter pss) throws SQLException {
        try (
            Connection connection = connectable.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            pss.setParameters(statement);
            statement.executeUpdate();
        }
    }

    public Object executeQueryWithPss(String query, PreparedStatementSetter pss, RowMapper rm) throws SQLException {
        try (
            Connection con = connectable.getConnection();
            PreparedStatement statement = con.prepareStatement(query)
        ) {
            pss.setParameters(statement);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return rm.mapRow(rs);
        }
    }

    public void executeBatchWithPss(String query, PreparedStatementSetter pss) throws SQLException {
        try (
            Connection con = connectable.getConnection();
            PreparedStatement statement = con.prepareStatement(query)
        ) {
            pss.setParameters(statement);
            statement.executeBatch();
        }
    }
}
