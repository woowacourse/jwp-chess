package chess.dao.jdbc;

import chess.dao.ScoreDao;
import chess.dao.dto.score.ScoreDto;
import chess.domain.manager.GameStatus;
import chess.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScoreDaoJDBC implements ScoreDao {

    @Override
    public Long save(final ScoreDto scoreDto) {
        final String query = "INSERT INTO score(game_id, white_score, black_score) VALUES (?, ?, ?)";

        try (final Connection connection = ConnectionProvider.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, scoreDto.getGameId().intValue());
            pstmt.setDouble(2, scoreDto.getWhiteScore());
            pstmt.setDouble(3, scoreDto.getBlackScore());
            return pstmt.executeLargeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("체스 게임의 점수를 저장하는데 실패했습니다.", e);
        }
    }

    @Override
    public Long update(ScoreDto scoreDto) {
        final String query = "UPDATE score SET white_score = ?, black_score = ? WHERE game_id = ?";

        try (final Connection connection = ConnectionProvider.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDouble(2, scoreDto.getWhiteScore());
            pstmt.setDouble(3, scoreDto.getBlackScore());
            pstmt.setLong(1, scoreDto.getGameId().intValue());
            return pstmt.executeLargeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("체스 게임의 점수를 저장하는데 실패했습니다.", e);
        }
    }

    @Override
    public ScoreDto findById(Long id) {
        final String query = "SELECT * from score where id = ?";

        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);) {
            pstmt.setInt(1, id.intValue());
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                return new ScoreDto(
                        resultSet.getLong("id"),
                        resultSet.getLong("game_id"),
                        resultSet.getDouble("white_score"),
                        resultSet.getDouble("black_score"));
            }
        } catch (SQLException e) {
            throw new DataAccessException("해당 GameID의 점수를 검색하는데 실패했습니다.", e);
        }
    }

    @Override
    public ScoreDto findByGameId(final Long gameId) {
        final String query = "SELECT * from score where game_id = ?";

        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);) {
            pstmt.setInt(1, gameId.intValue());
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                return new ScoreDto(
                        resultSet.getLong("id"),
                        resultSet.getLong("game_id"),
                        resultSet.getDouble("white_score"),
                        resultSet.getDouble("black_score"));
            }
        } catch (SQLException e) {
            throw new DataAccessException("해당 GameID의 점수를 검색하는데 실패했습니다.", e);
        }
    }
}
