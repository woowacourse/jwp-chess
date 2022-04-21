package chess.dao;

import chess.domain.pieces.Color;
import chess.domain.pieces.Piece;
import chess.domain.pieces.Symbol;
import chess.domain.position.Column;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChessPieceDao implements PieceDao<Piece> {

    private final ConnectionManager connectionManager;

    public ChessPieceDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Piece save(Piece piece) {
        final String sql = "INSERT INTO piece (type, color, position_id) VALUES (?, ?, ?)";
        return connectionManager.executeQuery(preparedStatement -> {
            preparedStatement.setString(1, piece.getType().symbol().name());
            preparedStatement.setString(2, piece.getColor().name());
            preparedStatement.setInt(3, piece.getPositionId());
            preparedStatement.executeUpdate();
            final ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new IllegalArgumentException(piece.getType().symbol().name() + " " + piece.getColor() + " 피스를 찾지 못했습니다.");
            }

            return new Piece(generatedKeys.getInt(1), piece.getColor(), piece.getType(), piece.getPositionId());
        }, sql);
    }

    @Override
    public Optional<Piece> findByPositionId(int positionId) {
        final String sql = "SELECT * FROM piece WHERE position_id=?";
        return connectionManager.executeQuery(preparedStatement -> {
            preparedStatement.setInt(1, positionId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }

            return Optional.of(makePiece(resultSet));
        }, sql);
    }

    @Override
    public int updatePositionId(int sourcePositionId, int targetPositionId) {
        final String sql = "UPDATE piece SET position_id=? WHERE position_id=?";
        return connectionManager.executeQuery(preparedStatement -> {
            preparedStatement.setInt(1, targetPositionId);
            preparedStatement.setInt(2, sourcePositionId);

            return preparedStatement.executeUpdate();
        }, sql);
    }

    @Override
    public int deleteByPositionId(int positionId) {
        final String sql = "DELETE FROM piece WHERE position_id=?";
        return connectionManager.executeQuery(preparedStatement -> {
            preparedStatement.setInt(1, positionId);

            return preparedStatement.executeUpdate();
        }, sql);
    }

    @Override
    public List<Piece> getAllByBoardId(int boardId) {
        final String sql = "SELECT pi.id, pi.type, pi.color, pi.position_id FROM piece pi JOIN position po ON pi.position_id=po.id " +
                "JOIN board nb ON po.board_id=nb.id WHERE nb.id=?";
        return connectionManager.executeQuery(preparedStatement -> {
            preparedStatement.setInt(1, boardId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            List<Piece> pieces = new ArrayList<>();
            while (resultSet.next()) {
                pieces.add(makePiece(resultSet));
            }

            return pieces;
        }, sql);
    }

    @Override
    public int countPawnsOnSameColumn(int roomId, Column column, Color color) {
        final String sql = "SELECT COUNT(*) AS total_count FROM piece pi " +
                "JOIN position po ON pi.position_id=po.id " +
                "WHERE po.position_column=? AND po.board_id=? AND pi.type='PAWN' AND pi.color=?";
        return connectionManager.executeQuery(preparedStatement -> {
            preparedStatement.setInt(1, column.value());
            preparedStatement.setInt(2, roomId);
            preparedStatement.setString(3, color.name());
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new IllegalArgumentException("쿼리가 올바르지 않습니다.");
            }

            return resultSet.getInt("total_count");
        }, sql);
    }

    private Piece makePiece(ResultSet resultSet) throws SQLException {
        return new Piece(
                resultSet.getInt("id"),
                Color.findColor(resultSet.getString("color")),
                Symbol.findSymbol(resultSet.getString("type")).type(),
                resultSet.getInt("position_id")
        );
    }
}
