package wooteco.chess.dao;

import wooteco.chess.database.MySqlConnector;
import wooteco.chess.dto.Commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class SelectJdbcTemplate {
    public List<Commands> select(String sql) {
        List<Commands> commands = new ArrayList<>();
        try (Connection connection = MySqlConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                Commands command = new Commands(rs.getString("command"));
                commands.add(command);
            }
            return commands;
        } catch (SQLException e) {
            System.err.println("연결 오류:" + e.getMessage());
            e.printStackTrace();
        }
        return commands;
    }
}
