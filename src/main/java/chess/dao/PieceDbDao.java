package chess.dao;

import chess.entity.PieceEntity;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("PieceDbDao")
public class PieceDbDao implements PieceDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<PieceEntity> pieceEntityRowMapper = (resultSet, rowNum) -> PieceEntity.of(
            resultSet.getInt("game_id"),
            resultSet.getString("piece_name"),
            resultSet.getString("piece_color"),
            resultSet.getString("position")
    );

    public PieceDbDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insert(PieceEntity pieceEntity) {
        String sql = "insert into piece (game_id, piece_name, piece_color, position) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                pieceEntity.getGameId(),
                pieceEntity.getPieceName(),
                pieceEntity.getPieceColor(),
                pieceEntity.getPosition());
    }

    @Override
    public PieceEntity find(PieceEntity pieceEntity) {
        String sql = "select * from piece where game_id=? and position=?";
        return jdbcTemplate.queryForObject(sql, pieceEntityRowMapper, pieceEntity.getGameId(), pieceEntity.getPosition());
    }

    @Override
    public List<PieceEntity> findAll(PieceEntity pieceEntity) {
        String sql = "SELECT * FROM piece WHERE game_id=?";
        return jdbcTemplate.query(sql, pieceEntityRowMapper, pieceEntity.getGameId());
    }

    @Override
    public void update(PieceEntity from, PieceEntity to) {
        String sql = "update piece set piece_name=?, piece_color=?, position=? where game_id=? and position=?";
        jdbcTemplate.update(sql,
                to.getPieceName(),
                to.getPieceColor(),
                to.getPosition(),
                from.getGameId(),
                from.getPosition());
    }

    @Override
    public void delete(PieceEntity pieceEntity) {
        String sql = "delete from piece where game_id=? and position=?";
        jdbcTemplate.update(sql, pieceEntity.getGameId(), pieceEntity.getPosition());
    }

    @Override
    public void deleteAll(PieceEntity pieceEntity) {
        String sql = "DELETE FROM piece WHERE game_id = ?";
        jdbcTemplate.update(sql, pieceEntity.getGameId());
    }
}
