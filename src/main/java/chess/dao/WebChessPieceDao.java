package chess.dao;

import chess.domain.pieces.Color;
import chess.domain.pieces.Piece;
import chess.domain.pieces.Symbol;
import chess.domain.pieces.Type;
import chess.domain.position.Column;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WebChessPieceDao implements PieceDao<Piece> {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public WebChessPieceDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Piece save(Piece piece) {
        final String sql = "INSERT INTO piece (type, color, position_id) VALUES (:type, :color, :position_id)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("type", piece.getType().symbol().name());
        parameters.put("color", piece.getColor().name());
        parameters.put("position_id", piece.getPositionId());
        SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
        jdbcTemplate.update(sql, namedParameters, keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();

        return new Piece(id, piece.getColor(), piece.getType(), piece.getPositionId());
    }

    @Override
    public Optional<Piece> findByPositionId(int positionId) {
        final String sql = "SELECT * FROM piece WHERE position_id=:position_id";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("position_id", positionId);
        SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
        Piece piece = jdbcTemplate.queryForObject(sql, namedParameters, (rs, rowNum) -> makePiece(rs));
        if (piece == null) {
            return Optional.empty();
        }
        return Optional.of(piece);
    }

    private Piece makePiece(ResultSet resultSet) throws SQLException {
        return new Piece(
                resultSet.getInt("id"),
                Color.findColor(resultSet.getString("color")),
                Symbol.findSymbol(resultSet.getString("type")).type(),
                resultSet.getInt("position_id")
        );
    }

    @Override
    public int updatePositionId(int sourcePositionId, int targetPositionId) {
        final String sql = "UPDATE piece SET position_id=:target_position_id WHERE position_id=:source_position_id";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("target_position_id", targetPositionId);
        parameters.put("source_position_id", sourcePositionId);
        SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
        return jdbcTemplate.update(sql, namedParameters);
    }

    @Override
    public int deleteByPositionId(int positionId) {
        final String sql = "DELETE FROM piece WHERE position_id=:position_id";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("position_id", positionId);
        SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
        return jdbcTemplate.update(sql, namedParameters);
    }

    @Override
    public List<Piece> getAllByBoardId(int boardId) {
        final String sql = "SELECT pi.id, pi.type, pi.color, pi.position_id FROM piece pi JOIN position po ON pi.position_id=po.id " +
                "JOIN board nb ON po.board_id=nb.id WHERE nb.id=:board_id";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("board_id", boardId);
        SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, namedParameters);
        List<Piece> result = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            int id = (int) map.get("id");
            String type = (String) map.get("type");
            String color = (String) map.get("color");
            int position_id = (int) map.get("position_id");

            result.add(new Piece(id, Color.findColor(color), Symbol.findSymbol(type).type(), position_id));
        }
        return result;
    }

    @Override
    public int countPawnsOnSameColumn(int boardId, Column column, Color color) {
        final String sql = "SELECT COUNT(*) AS total_count FROM piece pi " +
                "JOIN position po ON pi.position_id=po.id " +
                "WHERE po.position_column=:column AND po.board_id=:board_id AND pi.type='PAWN' AND pi.color=:color";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("column", column.value());
        parameters.put("board_id", boardId);
        parameters.put("color", color.name());
        SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);

        return jdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
    }
}
