package chess.web.dao.game;


import chess.web.dao.entity.ChessGameEntity;
import chess.web.dao.entity.GameStatusEntity;
import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ChessGameDAO implements ChessGameRepository {
    private final JdbcTemplate jdbcTemplate;

    public ChessGameDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ChessGameEntity> ChessGameEntityRowMapper = (resultSet, rowNum) ->
        new ChessGameEntity(
            resultSet.getLong("id"),
            resultSet.getString("title"),
            resultSet.getString("current_turn_team_color")
        );

    @Override
    public ChessGameEntity save(ChessGameEntity chessRoomEntity) {
        String query = "INSERT INTO chess_game (title, current_turn_team_color) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"});
            ps.setString(1, chessRoomEntity.getTitle());
            ps.setString(2, chessRoomEntity.getCurrentTurnTeamColor().getValue());
            return ps;
        }, keyHolder);
        chessRoomEntity.setId(keyHolder.getKey().longValue());
        return chessRoomEntity;
    }

    @Override
    public ChessGameEntity findById(Long id) {
        String query = "SELECT * FROM chess_game WHERE id = ?";
        return jdbcTemplate.queryForObject(query, ChessGameEntityRowMapper, id);
    }

    @Override
    public List<ChessGameEntity> findAll() {
        String query = "SELECT * FROM chess_game";
        return jdbcTemplate.query(query, ChessGameEntityRowMapper);
    }

    @Override
    public GameStatusEntity findStatusByGameId(Long gameId) {
        String query = "SELECT * FROM chess_game WHERE id = ?";
        return jdbcTemplate.queryForObject(
            query,
            (resultSet, rowNum) ->
                new GameStatusEntity(
                    resultSet.getString("title"),
                    resultSet.getString("current_turn_team_color")), gameId);
    }

    @Override
    public ChessGameEntity updateCurrentTurnTeamColor(ChessGameEntity chessGameEntity) {
        String query = "UPDATE chess_game SET current_turn_team_color = ? WHERE id = ?";
        jdbcTemplate.update(query, chessGameEntity.getCurrentTurnTeamColor().getValue(), chessGameEntity.getId());
        return chessGameEntity;
    }

    @Override
    public void remove(Long gameId) {
        String query = "DELETE FROM chess_game WHERE id = ?";
        jdbcTemplate.update(query, gameId);
    }

    @Override
    public void removeAll() {
        String query = "DELETE FROM chess_game";
        jdbcTemplate.update(query);
    }
}
