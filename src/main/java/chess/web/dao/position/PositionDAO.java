package chess.web.dao.position;


import chess.web.domain.position.Position;
import chess.web.domain.position.type.File;
import chess.web.domain.position.type.Rank;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PositionDAO implements PositionRepository {
    private final JdbcTemplate jdbcTemplate;

    public PositionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Position findByFileAndRank(File file, Rank rank) {
        String query = "SELECT * FROM position WHERE file_value = ? AND rank_value = ?";
        return jdbcTemplate.queryForObject(
            query,
            (resultSet, rowNum) -> new Position(
                resultSet.getLong("id"),
                resultSet.getString("file_value"),
                resultSet.getString("rank_value")
            ), file.getValue(), String.valueOf(rank.getValue()));
    }
}
