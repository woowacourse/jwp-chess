package chess.database.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import chess.database.entity.PieceEntity;
import chess.database.entity.PointEntity;

@Repository
public class JdbcPieceDao implements PieceDao {

    private final SimpleJdbcInsert jdbcInsert;
    private final JdbcTemplate jdbcTemplate;

    public JdbcPieceDao(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("piece")
            .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveBoard(List<PieceEntity> entities) {
        jdbcInsert.executeBatch(
            entities.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new)
        );
    }

    @Override
    public List<PieceEntity> findBoardByGameId(Long gameId) {
        final String sql = "SELECT * FROM piece WHERE game_id = ?";
        return jdbcTemplate.query(sql, (resultSet, rowMapper) -> new PieceEntity(
            resultSet.getString("piece_type"),
            resultSet.getString("piece_color"),
            resultSet.getInt("vertical_index"),
            resultSet.getInt("horizontal_index"),
            resultSet.getLong("game_id")
        ), gameId);
    }

    @Override
    public void deletePiece(PointEntity pointEntity, Long gameId) {
        final String sql = "DELETE FROM piece WHERE horizontal_index = ? AND vertical_index = ? AND game_id = ?";
        jdbcTemplate.update(sql, pointEntity.getHorizontalIndex(), pointEntity.getVerticalIndex(), gameId);
    }

    @Override
    public void updatePiece(PointEntity sourceEntity, PointEntity destinationEntity, Long gameId) {
        final String sql = "UPDATE piece SET horizontal_index = ?, vertical_index = ? "
            + "WHERE horizontal_index = ? AND vertical_index = ? AND game_id = ?";
        jdbcTemplate.update(sql,
            destinationEntity.getHorizontalIndex(), destinationEntity.getVerticalIndex(),
            sourceEntity.getHorizontalIndex(), sourceEntity.getVerticalIndex(), gameId);
    }

}
