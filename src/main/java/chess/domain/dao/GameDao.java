package chess.domain.dao;

import chess.domain.dto.GameDto;
import chess.domain.game.Status;
import chess.domain.game.board.ChessBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class GameDao {

    private static final int EMPTY_RESULT = 0;
    private final Connection connection;
    private int gameId = 0;
    private Connector connector = new Connector();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public GameDao() {
        connection = connector.makeConnection(Connector.PROD_DB_URL);
    }

    public GameDao(final Connection connection) {
        this.connection = connection;
    }

    public int save(ChessBoard chessBoard) {
        final String sql = "insert into Game (id, status, turn) values(?, ?, ?)";
        try {
            final PreparedStatement statement = makeSaveStatements(chessBoard, sql);
            statement.executeUpdate();
            statement.close();
            return gameId;
        } catch (Exception throwables) {
            logger.error(throwables.getMessage());
            throw new IllegalArgumentException("요청이 정상적으로 실행되지 않았습니다.");
        }
    }

    private PreparedStatement makeSaveStatements(ChessBoard chessBoard, String sql) {
        try {
            final PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, ++gameId);
            statement.setBoolean(2, chessBoard.compareStatus(Status.PLAYING));
            statement.setString(3, chessBoard.getCurrentTurn().name());
            return statement;
        } catch (Exception throwables) {
            logger.error(throwables.getMessage());
            throw new IllegalArgumentException("요청이 정상적으로 실행되지 않았습니다.");
        }
    }

    public int findLastGameId() {
        final String sql = "SELECT * FROM Game ORDER BY id DESC LIMIT 1";
        try {
            final PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet result = statement.executeQuery();
           if (!result.next()) {
                return EMPTY_RESULT;
            }
            return result.getInt("id");
        } catch (Exception throwables) {
            logger.error(throwables.getMessage());
            throw new IllegalArgumentException("요청이 정상적으로 실행되지 않았습니다.");
        }
    }

    public GameDto findById(int id) {
        final String sql = "select id, status, turn from game where id = ?";
        try {
            final PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, id);
            final ResultSet result = statement.executeQuery();
            if (!result.next()) {
                return null;
            }
            return new GameDto(
                    result.getInt("id"),
                    result.getBoolean("status"),
                    result.getString("turn")
            );
        } catch (Exception throwables) {
            logger.error(throwables.getMessage());
            throw new IllegalArgumentException("요청이 정상적으로 실행되지 않았습니다.");
        }
    }

    public int delete() {
        final String sql = "delete from game where id = ?";
        try {
            final PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int lastGameid = findLastGameId();
            statement.setInt(1, lastGameid);
            statement.executeUpdate();
            statement.close();
            return lastGameid;
        } catch (Exception throwables) {
            logger.error(throwables.getMessage());
            throw new IllegalArgumentException("요청이 정상적으로 실행되지 않았습니다.");
        }
    }
}
