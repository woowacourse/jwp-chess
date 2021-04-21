package chess.dao.jdbc;

import chess.dao.GameDao;
import chess.dao.dto.game.GameDto;
import chess.domain.game.Game;
import chess.exception.DataAccessException;

import java.sql.*;

public class GameDaoJDBC implements GameDao {

    @Override
    public Long save(final Game game) {
        final String query = "INSERT INTO game(room_name, white_username, black_username) VALUES (?, ?, ?)";

        try (final Connection connection = ConnectionProvider.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, game.roomName());
            pstmt.setString(2, game.whiteUsername());
            pstmt.setString(3, game.blackUsername());
            pstmt.executeLargeUpdate();
            ResultSet resultSet = pstmt.getGeneratedKeys();
            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            throw new DataAccessException("체스 게임을 저장하는데 실패했습니다.", e);
        }
    }

    @Override
    public GameDto findById(final Long gameId) {
        final String query = "SELECT * from game where id = ?";

        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);) {
            pstmt.setInt(1, gameId.intValue());
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                return new GameDto(
                        gameId,
                        resultSet.getString("white_username"),
                        resultSet.getString("black_username"),
                        resultSet.getString("room_name"));
            }
        } catch (SQLException e) {
            throw new DataAccessException("해당 ID의 체스게임을 검색하는데 실패했습니다.", e);
        }
    }
}
