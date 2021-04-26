package chess.dao.jdbc;

import chess.dao.HistoryDao;
import chess.dao.dto.history.HistoryDto;
import chess.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistoryDaoJDBC implements HistoryDao {

    @Override
    public Long save(final HistoryDto historyDto) {
        final String query =
                "INSERT INTO history(game_id, move_command, turn_owner, turn_number, playing) VALUES (?, ?, ?, ?, ?)";

        try (final Connection connection = ConnectionProvider.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, historyDto.getGameId().intValue());
            pstmt.setString(2, historyDto.getMoveCommand());
            pstmt.setString(3, historyDto.getTurnOwner());
            pstmt.setInt(4, historyDto.getTurnNumber());
            pstmt.setBoolean(5, historyDto.isPlaying());
            return pstmt.executeLargeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("체스게임의 기록을 저장하는데 실패했습니다.", e);
        }
    }

    @Override
    public List<HistoryDto> findByGameId(final Long gameId) {
        final String query = "SELECT * from history where game_id = ? ORDER BY id ASC";

        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);) {
            pstmt.setInt(1, gameId.intValue());
            try (ResultSet resultSet = pstmt.executeQuery()) {
                List<HistoryDto> historyDtos = new ArrayList<>();
                while (resultSet.next()) {
                    historyDtos.add(new HistoryDto(
                            resultSet.getLong("id"),
                            resultSet.getLong("game_id"),
                            resultSet.getString("move_command"),
                            resultSet.getString("turn_owner"),
                            resultSet.getInt("turn_number"),
                            resultSet.getBoolean("playing")
                    ));
                }
                return historyDtos;
            }
        } catch (SQLException e) {
            throw new DataAccessException("해당 GameID의 기록들을 검색하는데 실패했습니다.", e);
        }
    }
}
