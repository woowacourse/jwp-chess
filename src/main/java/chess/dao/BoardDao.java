package chess.dao;

import chess.domain.Color;
import chess.web.DBConnector;
import chess.web.dto.GameStateDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BoardDao implements BoardRepository {

    private static final String ERROR_DB_FAILED = "[ERROR] DB 연결에 문제가 발생했습니다.";

    @Override
    public int save(int roomId, GameStateDto gameStateDto) {
        final String sql = "insert into board (room_id, turn) values (?, ?)";

        try (final Connection connection = DBConnector.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, roomId);
            statement.setString(2, gameStateDto.getTurn());
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
    public Color getTurn(int boardId) {
        final String sql = "select turn from board where id = ?";
        Color color = Color.WHITE;

        try (final Connection connection = DBConnector.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, boardId);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    color = Color.valueOf(resultSet.getString("turn").toUpperCase());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(ERROR_DB_FAILED);
        }
        return color;
    }

    @Override
    public int getBoardIdByRoom(int roomId) {
        final String sql = "select id from board where room_id = ?";
        int id = 0;

        try (final Connection connection = DBConnector.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomId);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(ERROR_DB_FAILED);
        }
        return id;
    }

    @Override
    public void update(int boardId, GameStateDto gameStateDto) {
        final String sql = "update board set turn = ? where id = ?";

        try (final Connection connection = DBConnector.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, gameStateDto.getTurn());
            statement.setInt(2, boardId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(ERROR_DB_FAILED);
        }
    }

    @Override
    public void deleteByRoom(int roomId) {
        final String sql = "delete from board where room_id = ?";

        try (final Connection connection = DBConnector.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(ERROR_DB_FAILED);
        }
    }

}
