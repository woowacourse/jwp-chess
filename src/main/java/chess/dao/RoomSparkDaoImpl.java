package chess.dao;

import chess.dao.exception.DeleteQueryException;
import chess.dao.exception.InsertQueryException;
import chess.dao.exception.SelectQueryException;
import chess.dao.exception.UpdateQueryException;
import chess.database.DBConnection;
import chess.domain.Team;
import chess.dto.RoomDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomSparkDaoImpl implements RoomDao{
    final Connection connection = DBConnection.getConnection();

    @Override
    public RoomDto findById(long roomId) {
        final String sql = "select * from room  where id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, roomId);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            Long id = resultSet.getLong("id");
            Team status = Team.valueOf(resultSet.getString("status"));
            return new RoomDto(id, status);
        } catch (SQLException e) {
            throw new SelectQueryException();
        }
    }

    @Override
    public void delete(long roomId) {
        final String sql = "delete from room where id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, roomId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DeleteQueryException();
        }
    }
    @Override
    public void save(long roomId, Team team) {
        final String sql = "insert into room (id, status) values(?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, roomId);
            statement.setString(2, team.name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new InsertQueryException();
        }
    }
    @Override
    public void updateStatus(Team team, long roomId) {
        final String sql = "update room set status = ? where id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, team.toString());
            statement.setLong(2, roomId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new UpdateQueryException();
        }
    }
}
