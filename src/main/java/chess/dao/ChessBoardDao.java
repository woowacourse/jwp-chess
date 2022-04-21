package chess.dao;

import chess.domain.game.ChessBoard;
import chess.domain.pieces.Color;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChessBoardDao implements BoardDao<ChessBoard> {

    private final ConnectionManager connectionManager;

    public ChessBoardDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public ChessBoard save(ChessBoard board) {
        final String sql = "INSERT INTO board (room_title, turn) VALUES (?, ?)";
        return connectionManager.executeQuery(preparedStatement -> {
            preparedStatement.setString(1, board.getRoomTitle());
            preparedStatement.setString(2, board.getTurn().name());
            preparedStatement.executeUpdate();
            final ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new IllegalArgumentException(board.getRoomTitle() + " 보드를 찾을 수 없습니다.");
            }

            return new ChessBoard(generatedKeys.getInt(1), board.getRoomTitle(), board.getTurn());
        }, sql);
    }

    @Override
    public ChessBoard getById(int id) {
        final String sql = "SELECT * FROM board WHERE id=?";
        return connectionManager.executeQuery(preparedStatement -> {
            preparedStatement.setInt(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new IllegalArgumentException(id + " 보드를 찾을 수 없습니다.");
            }

            return makeBoard(resultSet, new ChessMemberDao(connectionManager));
        }, sql);
    }

    @Override
    public List<ChessBoard> findAll() {
        final String sql = "SELECT * FROM board";
        return connectionManager.executeQuery(preparedStatement -> {
            final ResultSet resultSet = preparedStatement.executeQuery();
            List<ChessBoard> boards = new ArrayList<>();
            while (resultSet.next()) {
                boards.add(makeBoard(resultSet, new ChessMemberDao(connectionManager)));
            }

            return boards;
        }, sql);
    }

    @Override
    public int deleteById(int id) {
        String sql = "DELETE FROM board where id=?";
        return connectionManager.executeQuery(preparedStatement -> {
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate();
        }, sql);
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM board";
        connectionManager.executeQuery(PreparedStatement::executeUpdate, sql);
    }

    @Override
    public void updateTurn(Color color, int boardId) {
        String sql = "UPDATE board SET turn=? WHERE id=?";
        connectionManager.executeQuery(preparedStatement -> {
            preparedStatement.setString(1, color.name());
            preparedStatement.setInt(2, boardId);

            return preparedStatement.executeUpdate();
        }, sql);
    }

    private ChessBoard makeBoard(ResultSet resultSet, ChessMemberDao chessMemberDao) throws SQLException {
        return new ChessBoard(
                resultSet.getInt("id"),
                resultSet.getString("room_title"),
                Color.findColor(resultSet.getString("turn")),
                chessMemberDao.getAllByBoardId(resultSet.getInt("id")));
    }
}
