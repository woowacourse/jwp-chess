package wooteco.chess.dao;

import wooteco.chess.connection.Connector;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.position.Position;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class BoardDAO {

    public BoardDAO() {
    }

    public void insertPiece(final Long roomId, final Board board, final Position position) throws SQLException {
        String query = "INSERT INTO board (roomId, position, piece) VALUES (?, ?, ?)";

        try (final Connection connection = Connector.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, roomId);
            preparedStatement.setString(2, position.toString());
            preparedStatement.setString(3, board.findBy(position).getName());
            preparedStatement.executeUpdate();
        }
    }

    public void updatePiece(final Long roomId, final String position, final String piece) throws SQLException {
        String query = "UPDATE board SET piece = (?) where roomId = (?) AND position = (?)";

        try (final Connection connection = Connector.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, piece);
            preparedStatement.setLong(2, roomId);
            preparedStatement.setString(3, position);
            preparedStatement.executeUpdate();
        }
    }

    public Map<String, String> findAllById(Long roomId) throws SQLException {
        String query = "SELECT * FROM board where roomId = (?)";
        try (final Connection connection = Connector.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, roomId);
            ResultSet resultSet = preparedStatement.executeQuery();

            Map<String, String> boardDto = new HashMap<>();
            while (resultSet.next()) {
                String piece = resultSet.getString("piece");
                String position = resultSet.getString("position");
                boardDto.put(position, piece);
            }
            return boardDto;
        }
    }

    public void deleteBoardById(Long roomId) throws SQLException {
        String query = "DELETE FROM board where roomId = (?)";
        try (final Connection connection = Connector.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, roomId);
            preparedStatement.executeUpdate();
        }
    }
}

