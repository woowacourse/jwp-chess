package chess.dao;

import chess.controller.dto.response.PieceResponse;
import chess.domain.board.Column;
import chess.domain.board.Position;
import chess.domain.board.Row;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
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
public class PieceDao {

    private static final int ROW_INDEX = 0;
    private static final int COLUMN_INDEX = 1;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertPiece;

    private final RowMapper<PieceResponse> pieceResponseRowMapper = (resultSet, rowNum) -> {
        Position position = parseStringToPosition(resultSet.getString("position"));
        PieceType pieceType = PieceType.valueOf(resultSet.getString("type"));
        Color color = Color.valueOf(resultSet.getString("color"));
        Piece piece = pieceType.createPiece(color);
        return new PieceResponse(position, piece);
    };

    private final RowMapper<Piece> pieceRowMapper = (resultSet, rowNum) -> {
        PieceType pieceType = PieceType.valueOf(resultSet.getString("type"));
        Color color = Color.valueOf(resultSet.getString("color"));
        return pieceType.createPiece(color);
    };

    public PieceDao(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertPiece = new SimpleJdbcInsert(dataSource)
                .withTableName("piece");
    }

    public void save(Long gameId, Position position, Piece piece) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("game_id", gameId)
                .addValue("position", convertPositionToString(position))
                .addValue("type", piece.getPieceType())
                .addValue("color", piece.getColor());
        insertPiece.execute(namedParameters);
    }

    private String convertPositionToString(Position position) {
        Column column = position.getColumn();
        Row row = position.getRow();
        return column.name().toLowerCase(Locale.ROOT) + row.getValue();
    }

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

    public Optional<Piece> find(Long gameId, Position position) {
        String sql = "SELECT type, color FROM piece WHERE game_id = :game_id AND position = :position";
        SqlParameterSource namedParameters = new MapSqlParameterSource("game_id", gameId)
                .addValue("position", convertPositionToString(position));
        try {
            return Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject(sql, namedParameters, pieceRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void updatePosition(Long gameId, Position start, Position target) {
        String sql = "UPDATE piece SET position = :target WHERE game_id = :game_id AND position = :start";
        SqlParameterSource namedParameters = new MapSqlParameterSource("game_id", gameId)
                .addValue("start", convertPositionToString(start))
                .addValue("target", convertPositionToString(target));
        namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    public void deleteByGameIdAndPosition(Long gameId, Position position) {
        String sql = "DELETE FROM piece WHERE game_id = :game_id AND position = :position";
        SqlParameterSource namedParameters = new MapSqlParameterSource("game_id", gameId)
                .addValue("position", convertPositionToString(position));
        namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    public void deleteByGameId(Long gameId) {
        String sql = "DELETE FROM piece WHERE game_id = :game_id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("game_id", gameId);
        namedParameterJdbcTemplate.update(sql, namedParameters);
    }
}
