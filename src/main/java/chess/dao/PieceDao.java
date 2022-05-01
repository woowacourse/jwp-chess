package chess.dao;

import chess.entity.PieceEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PieceDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<PieceEntity> pieceEntityRowMapper = (resultSet, rowNum) -> PieceEntity.of(
            resultSet.getInt("game_id"),
            resultSet.getString("piece_name"),
            resultSet.getString("piece_color"),
            resultSet.getString("position")
    );

    public PieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(PieceEntity pieceEntity) {
        String sql = "insert into piece (game_id, piece_name, piece_color, position) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                pieceEntity.getGameId(),
                pieceEntity.getPieceName(),
                pieceEntity.getPieceColor(),
                pieceEntity.getPosition());
    }

    public PieceEntity find(PieceEntity pieceEntity) {
        String sql = "select * from piece where game_id=? and position=?";
        return jdbcTemplate.queryForObject(sql, pieceEntityRowMapper, pieceEntity.getGameId(), pieceEntity.getPosition());
    }

    public List<PieceEntity> findAll(PieceEntity pieceEntity) {
        String sql = "SELECT * FROM piece WHERE game_id=?";
        return jdbcTemplate.query(sql, pieceEntityRowMapper, pieceEntity.getGameId());
    }

    public void update(PieceEntity from, PieceEntity to) {
        String sql = "update piece set piece_name=?, piece_color=?, position=? where game_id=? and position=?";
        jdbcTemplate.update(sql,
                to.getPieceName(),
                to.getPieceColor(),
                to.getPosition(),
                from.getGameId(),
                from.getPosition());
    }

    public void delete(PieceEntity pieceEntity) {
        String sql = "delete from piece where game_id=? and position=?";
        jdbcTemplate.update(sql, pieceEntity.getGameId(), pieceEntity.getPosition());
    }

    public void deleteAll(PieceEntity pieceEntity) {
        String sql = "DELETE FROM piece WHERE game_id = ?";
        jdbcTemplate.update(sql, pieceEntity.getGameId());
    }
}
