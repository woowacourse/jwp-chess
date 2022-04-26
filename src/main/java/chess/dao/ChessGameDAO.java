package chess.dao;

import chess.dto.ChessGameRoomInfoDTO;
import chess.dto.GameCreationDTO;
import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ChessGameDAO {

    private static final RowMapper<ChessGameRoomInfoDTO> CHESS_GAME_ROOM_INFO_DTO_ROW_MAPPER = (resultSet, rowNumber) ->
            new ChessGameRoomInfoDTO(
                    resultSet.getString("id"),
                    resultSet.getString("name"),
                    resultSet.getString("password")
            );

    private final JdbcTemplate jdbcTemplate;

    public ChessGameDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long addGame(final GameCreationDTO gameCreationDTO) {
        String sql = "INSERT INTO CHESS_GAME (name, password) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, new String[]{"id"});
            statement.setString(1, gameCreationDTO.getName());
            statement.setString(2, gameCreationDTO.getPassword());
            return statement;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public List<ChessGameRoomInfoDTO> findActiveGames() {
        String sql = "SELECT id, name, password FROM CHESS_GAME WHERE IS_END = false";
        return jdbcTemplate.query(sql, CHESS_GAME_ROOM_INFO_DTO_ROW_MAPPER);
    }

    public ChessGameRoomInfoDTO findGameById(final String gameId) {
        String sql = "SELECT id, name, password FROM CHESS_GAME WHERE ID = ? AND IS_END = FALSE ORDER BY created_at";
        return jdbcTemplate.queryForObject(sql, CHESS_GAME_ROOM_INFO_DTO_ROW_MAPPER, gameId);
    }

    public void updateGameEnd(final String gameId) {
        String sql = "UPDATE chess_game SET is_end = true WHERE id = ?";
        jdbcTemplate.update(sql, gameId);
    }

    public void deleteGame(final String gameId) {
        String sql = "DELETE FROM CHESS_GAME WHERE ID = ?";
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, Long.parseLong(gameId));
            return statement;
        });
    }
}
