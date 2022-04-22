package chess.dao;

import chess.web.DBConnector;
import chess.web.dto.RoomDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import org.springframework.stereotype.Repository;

public class RoomDao implements RoomRepository {

    private static final String ERROR_DB_FAILED = "[ERROR] DB 연결에 문제가 발생했습니다.";

    @Override
    public int save(String name) {
        final Connection connection = DBConnector.getConnection();
        final String sql = "insert into room (name) values (?)";

        try (final PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(ERROR_DB_FAILED);
        }
        throw new RuntimeException(ERROR_DB_FAILED);
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
