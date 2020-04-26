package wooteco.chess.dao;

import wooteco.chess.domain.position.MovingPosition;
import wooteco.chess.web.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseHistoryDao implements HistoryDao {
    private final ConnectionManager connectionManager = new ConnectionManager();

    @Override
    public void insert(MovingPosition movingPosition) throws SQLException {
        String query = "INSERT INTO history (start, end) VALUES (?, ?)";

        try (PreparedStatement pstmt = connectionManager.getConnection().prepareStatement(query)) {
            pstmt.setString(1, movingPosition.getStart());
            pstmt.setString(2, movingPosition.getEnd());
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<MovingPosition> selectAll() throws SQLException {
        String query = "SELECT * FROM history";

        try (PreparedStatement pstmt = connectionManager.getConnection().prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
            List<MovingPosition> result = new ArrayList<>();
            while (rs.next()) {
                result.add(new MovingPosition(rs.getString("start"), rs.getString("end")));
            }
            return Collections.unmodifiableList(result);
        }
    }

    @Override
    public void clear() throws SQLException {
        String query = "DELETE FROM history";

        try (PreparedStatement pstmt = connectionManager.getConnection().prepareStatement(query)) {
            pstmt.executeUpdate();
        }
    }
}