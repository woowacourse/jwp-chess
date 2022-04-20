package springchess.dao;

import org.springframework.stereotype.Repository;
import springchess.model.board.Board;
import springchess.model.piece.Piece;
import springchess.model.piece.Team;
import springchess.model.square.Square;
import springchess.model.status.End;
import springchess.model.status.StatusType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

@Repository
public class ChessBoardDao implements BoardDao<Board> {

    private final ConnectionManager connectionManager;

    public ChessBoardDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Board save(Board board) {
        return connectionManager.executeQuery(connection -> {
            final String sql = "INSERT INTO board (status, team) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, board.getStatus().name());
            preparedStatement.setString(2, board.getTeam().name());
            preparedStatement.executeUpdate();
            final ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new IllegalArgumentException("보드가 없습니다. 방 제목: " + board.getStatus());
            }
            return new Board(
                    generatedKeys.getInt(1),
                    board.getStatus(),
                    board.getTeam());
        });
    }

    @Override
    public int deleteAll() {
        return connectionManager.executeQuery(connection -> {
            String sql = "DELETE FROM board";
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            return preparedStatement.executeUpdate();
        });
    }

    @Override
    public int deleteById(int id) {
        return connectionManager.executeQuery(connection -> {
            String sql = "DELETE FROM board WHERE id=?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate();
        });
    }

    @Override
    public Board getById(int id) {
        return connectionManager.executeQuery(connection -> {
            final String sql = "SELECT * FROM board WHERE id=?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            final ChessMemberDao chessMemberDao = new ChessMemberDao(new ConnectionManager());
            if (!resultSet.next()) {
                throw new IllegalArgumentException("그런 보드 없습니다. 방 id: " + id);
            }
            return new Board(
                    resultSet.getInt("id"),
                    StatusType.findStatus(resultSet.getString("status")),
                    Team.findTeam(resultSet.getString("team"))
            );
        });

    }

    @Override
    public Board init(Board board, Map<Square, Piece> startingPieces) {
        return connectionManager.executeQuery(connection -> {
            final Board savedBoard = save(board);
            final ChessSquareDao chessSquareDao = new ChessSquareDao(connectionManager);
            final ChessPieceDao chessPieceDao = new ChessPieceDao(connectionManager);
            chessSquareDao.saveAllSquare(savedBoard.getId());
            for (Square square : startingPieces.keySet()) {
                int squareId = chessSquareDao.getSquareIdBySquare(square, savedBoard.getId());
                chessPieceDao.save(startingPieces.get(square), squareId);
            }
            return savedBoard;
        });
    }

    @Override
    public int finishGame(int boardId) {
        return connectionManager.executeQuery(connection -> {
            String sql = "UPDATE board SET status=? where id=?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, new End().name());
            preparedStatement.setInt(2, boardId);
            return preparedStatement.executeUpdate();
        });
    }

    @Override
    public boolean isEnd(int boardId) {
        return connectionManager.executeQuery(connection -> {
            String sql = "SELECT status FROM board where id=?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, boardId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new IllegalArgumentException("그런 보드 없습니다. 방 id: " + boardId);
            }
            return resultSet.getString("status").equals(new End().name());
        });
    }

    @Override
    public int changeTurn(int boardId) {
        return connectionManager.executeQuery(connection -> {
            String sql = "UPDATE board SET team = if( team = 'WHITE', 'BLACK', 'WHITE') WHERE id=?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, boardId);
            return preparedStatement.executeUpdate();
        });
    }
}
