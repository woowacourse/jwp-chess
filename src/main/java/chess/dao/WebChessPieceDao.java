package chess.dao;

import chess.domain.pieces.Color;
import chess.domain.pieces.Piece;
import chess.domain.pieces.Symbol;
import chess.domain.position.Column;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WebChessPieceDao implements PieceDao<Piece> {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public WebChessPieceDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Piece save(Piece piece) {
        final String sql = "INSERT INTO piece (type, color, position_id) VALUES (:type, :color, :position_id)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        List<String> keys = List.of("type", "color", "position_id");
        List<Object> values = List.of(piece.getType().symbol().name(), piece.getColor().name(), piece.getPositionId());

        jdbcTemplate.update(sql, ParameterSourceCreator.makeParameterSource(keys, values), keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();

        return new Piece(id, piece.getColor(), piece.getType(), piece.getPositionId());
    }

    @Override
    public Optional<Piece> findByPositionId(int positionId) {
        final String sql = "SELECT * FROM piece WHERE position_id=:position_id";
        List<String> keys = List.of("position_id");
        List<Object> values = List.of(positionId);
        SqlParameterSource namedParameters = ParameterSourceCreator.makeParameterSource(keys, values);
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

        List<String> keys = List.of("target_position_id", "source_position_id");
        List<Object> values = List.of(targetPositionId, sourcePositionId);
        return jdbcTemplate.update(sql, ParameterSourceCreator.makeParameterSource(keys, values));
    }

    @Override
    public int deleteByPositionId(int positionId) {
        final String sql = "DELETE FROM piece WHERE position_id=:position_id";
        List<String> keys = List.of("position_id");
        List<Object> values = List.of(positionId);
        SqlParameterSource namedParameters = ParameterSourceCreator.makeParameterSource(keys, values);
        return jdbcTemplate.update(sql, namedParameters);
    }

    @Override
    public List<Piece> getAllByBoardId(int boardId) {
        final String sql =
                "SELECT pi.id, pi.type, pi.color, pi.position_id FROM piece pi JOIN position po ON pi.position_id=po.id "
                        + "JOIN board nb ON po.board_id=nb.id WHERE nb.id=:board_id";
        List<String> keys = List.of("board_id");
        List<Object> values = List.of(boardId);
        SqlParameterSource namedParameters = ParameterSourceCreator.makeParameterSource(keys, values);
        return jdbcTemplate.query(sql, namedParameters, (rs, rowNum) -> makePiece(rs));
    }

    @Override
    public int countPawnsOnSameColumn(int boardId, Column column, Color color) {
        final String sql = "SELECT COUNT(*) AS total_count FROM piece pi " +
                "JOIN position po ON pi.position_id=po.id " +
                "WHERE po.position_column=:column AND po.board_id=:board_id AND pi.type='PAWN' AND pi.color=:color";

        List<String> keys = List.of("column", "board_id", "color");
        List<Object> values = List.of(column.value(), boardId, color.name());
        SqlParameterSource namedParameters = ParameterSourceCreator.makeParameterSource(keys, values);

        return jdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
    }
}
