package chess.dao.jdbc;

import chess.dao.GameDao;
import chess.dao.dto.game.GameDto;
import chess.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDaoJDBC implements GameDao {

    @Override
    public Long save(final GameDto gameDto) {
        final String query = "INSERT INTO game(room_name, white_username, black_username) VALUES (?, ?, ?)";

        try (final Connection connection = ConnectionProvider.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, gameDto.getRoomName());
            pstmt.setString(2, gameDto.getWhiteUsername());
            pstmt.setString(3, gameDto.getBlackUsername());
            pstmt.executeLargeUpdate();
            ResultSet resultSet = pstmt.getGeneratedKeys();
            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            throw new DataAccessException("체스 게임을 저장하는데 실패했습니다.", e);
        }
    }

    @Override
    public Long update(GameDto gameDto) {
        final String query = "UPDATE game SET room_name = ?, white_username = ?, black_username = ? WHERE id = ?";

        try (final Connection connection = ConnectionProvider.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, gameDto.getRoomName());
            pstmt.setString(2, gameDto.getWhiteUsername());
            pstmt.setString(3, gameDto.getBlackUsername());
            pstmt.setLong(4, gameDto.getId());
            pstmt.executeLargeUpdate();
            ResultSet resultSet = pstmt.getGeneratedKeys();
            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            throw new DataAccessException("체스 게임을 업데이트 하는데 실패했습니다.", e);
        }
    }

    @Override
    public List<GameDto> findByPlayingIsTrue() {
        final String query = "SELECT * from game AS g JOIN state AS s ON g.id = s.game_id WHERE s.playing = true ORDER BY g.id ASC";

        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);) {
            try (ResultSet resultSet = pstmt.executeQuery()) {
                List<GameDto> gameDtos = new ArrayList<>();
                while (!resultSet.next()) {
                    gameDtos.add(new GameDto(
                            resultSet.getLong("id"),
                            resultSet.getString("room_name"),
                            resultSet.getString("white_username"),
                            resultSet.getString("black_username")));
                }
                return gameDtos;
            }
        } catch (SQLException e) {
            throw new DataAccessException("해당 ID의 체스게임을 검색하는데 실패했습니다.", e);
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
                        resultSet.getLong("id"),
                        resultSet.getString("room_name"),
                        resultSet.getString("white_username"),
                        resultSet.getString("black_username"));
            }
        } catch (SQLException e) {
            throw new DataAccessException("해당 ID의 체스게임을 검색하는데 실패했습니다.", e);
        }
    }
}
