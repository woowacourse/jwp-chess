package wooteco.chess.domain.dao;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.connection.Connector;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceType;
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

    public void insertPiece(final Board board, final Position position) throws SQLException {
        String query = "INSERT INTO board (position, piece) VALUES (?, ?)";

        try (final Connection connection = Connector.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, position.toString());
            preparedStatement.setString(2, board.findBy(position).getName());
            preparedStatement.executeUpdate();
        }
    }

    public void updatePiece(final Board board, final Position position) throws SQLException {
        String query = "UPDATE board SET piece = (?) where position = (?)";

        try (final Connection connection = Connector.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, board.findBy(position).getName());
            preparedStatement.setString(2, position.toString());
            preparedStatement.executeUpdate();
        }
    }

    public void deletePieces() throws SQLException {
        String query = "TRUNCATE board";

        try (final Connection connection = Connector.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }
    }

    public Map<Position, Piece> findAllPieces() throws SQLException {
        String query = "SELECT * FROM board";
        try (final Connection connection = Connector.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            Map<Position, Piece> output = new HashMap<>();
            while (resultSet.next()) {
                Piece piece = Piece.of(PieceType.valueOf(resultSet.getString("piece")));
                Position position = Position.of(resultSet.getString("position"));
                output.put(position, piece);
            }
            return output;
        }
    }
}

