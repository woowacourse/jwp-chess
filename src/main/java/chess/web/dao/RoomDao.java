package chess.web.dao;

import chess.web.DBConnector;
import chess.web.dto.RoomDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class RoomDao implements RoomRepository {

    @Override
    public void save(String name) {
        final Connection connection = DBConnector.getConnection();
        final String sql = "insert into room (name) values (?)";

        try (final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<RoomDto> find(String name) {
        final Connection connection = DBConnector.getConnection();
        final String sql = "select * from room where name = ?";
        Optional<RoomDto> roomDto = Optional.ofNullable(null);
        try (final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                roomDto = Optional.of(new RoomDto(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roomDto;
    }
}
