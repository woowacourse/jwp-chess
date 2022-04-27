package chess.dao;

import chess.controller.dto.response.PieceResponse;
import chess.domain.board.Column;
import chess.domain.board.Position;
import chess.domain.board.Row;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class PieceDaoImpl implements PieceDao {

    private static final int ROW_INDEX = 0;
    private static final int COLUMN_INDEX = 1;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertPiece;

    private final RowMapper<PieceResponse> pieceResponseRowMapper = (resultSet, rowNum) -> {
        Position position = parseStringToPosition(resultSet.getString("position"));
        PieceType pieceType = PieceType.valueOf(resultSet.getString("piece_type"));
        Color color = Color.valueOf(resultSet.getString("color"));
        Piece piece = pieceType.createPiece(color);
        return new PieceResponse(position, piece);
    };

    private final RowMapper<Piece> pieceRowMapper = (resultSet, rowNum) -> {
        PieceType pieceType = PieceType.valueOf(resultSet.getString("piece_type"));
        Color color = Color.valueOf(resultSet.getString("color"));
        return pieceType.createPiece(color);
    };

    public PieceDaoImpl(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertPiece = new SimpleJdbcInsert(dataSource)
                .withTableName("piece");
    }

    @Override
    public void save(Long gameId, Position position, Piece piece) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("game_id", gameId);
        parameters.put("position", convertPositionToString(position));
        parameters.put("piece_type", piece.getPieceType());
        parameters.put("color", piece.getColor());
        insertPiece.execute(parameters);
    }

    private String convertPositionToString(Position position) {
        Column column = position.getColumn();
        Row row = position.getRow();
        return column.name().toLowerCase(Locale.ROOT) + row.getValue();
    }

    @Override
    public List<PieceResponse> findAll(Long gameId) {
        String sql = "SELECT * FROM piece WHERE game_id = :game_id";

        SqlParameterSource namedParameters = new MapSqlParameterSource("game_id", gameId);

        try {
            return namedParameterJdbcTemplate.query(sql, namedParameters, pieceResponseRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    private Position parseStringToPosition(String rawPosition) {
        String[] separatedPosition = rawPosition.split("");
        Column column = Column.from(separatedPosition[ROW_INDEX]);
        Row row = Row.from(separatedPosition[COLUMN_INDEX]);
        return new Position(column, row);
    }

    @Override
    public Optional<Piece> find(Long gameId, Position position) {
        String sql = "SELECT piece_type, color FROM piece WHERE game_id = :game_id AND position = :position";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("game_id", gameId);
        parameters.put("position", convertPositionToString(position));

        try {
            return Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject(sql, parameters, pieceRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void updatePosition(Long gameId, Position start, Position target) {
        String sql = "UPDATE piece SET position = :target WHERE game_id = :game_id AND position = :start";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("game_id", gameId);
        parameters.put("start", convertPositionToString(start));
        parameters.put("target", convertPositionToString(target));
        namedParameterJdbcTemplate.update(sql, parameters);
    }

    @Override
    public void delete(Long gameId, Position position) {
        String sql = "DELETE FROM piece WHERE game_id = :game_id AND position = :position";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("game_id", gameId);
        parameters.put("position", convertPositionToString(position));
        namedParameterJdbcTemplate.update(sql, parameters);
    }
}
