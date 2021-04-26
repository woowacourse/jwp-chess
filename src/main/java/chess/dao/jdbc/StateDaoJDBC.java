package chess.dao.jdbc;

import chess.dao.StateDao;
import chess.dao.dto.state.StateDto;
import chess.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StateDaoJDBC implements StateDao {

    @Override
    public Long save(final StateDto stateDto) {
        final String query = "INSERT INTO state(game_id, turn_owner, turn_number, playing) VALUES (?, ?, ?, ?)";

        try (final Connection connection = ConnectionProvider.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, stateDto.getGameId().intValue());
            pstmt.setString(2, stateDto.getTurnOwner());
            pstmt.setInt(3, stateDto.getTurnNumber());
            pstmt.setBoolean(4, stateDto.isPlaying());
            return pstmt.executeLargeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("체스게임의 상태를 저장하는데 실패했습니다.", e);
        }
    }

    @Override
    public Long update(StateDto stateDto) {
        final String query = "UPDATE state SET turn_owner=?, turn_number=?, playing=? WHERE game_id=?";

        try (final Connection connection = ConnectionProvider.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, stateDto.getTurnOwner());
            pstmt.setInt(2, stateDto.getTurnNumber());
            pstmt.setBoolean(3, stateDto.isPlaying());
            pstmt.setInt(4, stateDto.getGameId().intValue());
            return pstmt.executeLargeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("체스게임의 상태를 업데이트하는데 실패했습니다.", e);
        }
    }

    @Override
    public StateDto findByGameId(final Long gameId) {
        final String query = "SELECT * from state where game_id = ?";

        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);) {
            pstmt.setInt(1, gameId.intValue());
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                return new StateDto(
                        resultSet.getLong("id"),
                        resultSet.getLong("game_id"),
                        resultSet.getString("turn_owner"),
                        resultSet.getInt("turn_number"),
                        resultSet.getBoolean("playing"));
            }
        } catch (SQLException e) {
            throw new DataAccessException("해당 GameID의 상태를 검색하는데 실패했습니다.", e);
        }
    }

    @Override
    public StateDto findById(final Long id) {
        final String query = "SELECT * from state where id = ?";

        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);) {
            pstmt.setInt(1, id.intValue());
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                return new StateDto(
                        resultSet.getLong("game_id"),
                        resultSet.getString("turn_owner"),
                        resultSet.getInt("turn_number"),
                        resultSet.getBoolean("playing"));
            }
        } catch (SQLException e) {
            throw new DataAccessException("해당 GameID의 상태를 검색하는데 실패했습니다.", e);
        }
    }
}
