package chess.dao;

import chess.domain.pieces.Color;
import chess.domain.pieces.Piece;
import chess.domain.pieces.Symbol;
import chess.domain.position.Column;
import chess.domain.position.Position;
import chess.domain.position.Row;
import chess.entities.PositionEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ChessPositionDao implements PositionDao<Position> {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ChessPositionDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public PositionEntity save(PositionEntity positionEntity) {
        final String sql = "INSERT INTO position (position_column, position_row, board_id) VALUES (:position_column, :position_row, :board_id)";

        List<String> keys = List.of("position_column", "position_row", "board_id");
        List<Object> values = List.of(positionEntity.getColumn().value(), positionEntity.getRow().value(),
                positionEntity.getBoardId());
        SqlParameterSource namedParameters = ParameterSourceCreator.makeParameterSource(keys, values);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, namedParameters, keyHolder);

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return new PositionEntity(id, positionEntity.getColumn(), positionEntity.getRow(), positionEntity.getBoardId());
    }

    @Override
    public PositionEntity getByColumnAndRowAndBoardId(Column column, Row row, int boardId) {
        final String sql = "SELECT id, position_column, position_row, board_id " +
                "FROM position " +
                "WHERE position_column=:position_column AND position_row=:position_row AND board_id=:board_id";

        List<String> keys = List.of("position_column", "position_row", "board_id");
        List<Object> values = List.of(column.value(), row.value(), boardId);
        SqlParameterSource namedParameters = ParameterSourceCreator.makeParameterSource(keys, values);

        return jdbcTemplate.queryForObject(sql, namedParameters, (rs, rowNum) -> makePosition(rs, "id"));
    }

    @Override
    public int saveAll(int boardId) {
        final String sql = "INSERT INTO POSITION (position_column, position_row, board_id) values (:position_column, :position_row, :board_id)";
        Column[] columns = Column.values();
        Row[] rows = Row.values();
        int boardSize = columns.length * rows.length;
        List<Map<String, Object>> batchValues = new ArrayList<>(boardSize);
        for (Column column : columns) {
            saveColumnLine(boardId, rows, batchValues, column);
        }
        return jdbcTemplate.batchUpdate(sql, batchValues.toArray(new Map[boardSize])).length;
    }

    private void saveColumnLine(int boardId, Row[] rows, List<Map<String, Object>> batchValues, Column column) {
        for (Row row : rows) {
            batchValues.add(
                    new MapSqlParameterSource("position_column", column.value())
                            .addValue("position_row", row.value())
                            .addValue("board_id", boardId)
                            .getValues());
        }
    }

    @Override
    public int getIdByColumnAndRowAndBoardId(Column column, Row row, int boardId) {
        return getByColumnAndRowAndBoardId(column, row, boardId).getId();
    }

    @Override
    public Map<Position, Piece> findAllPositionsAndPieces(int boardId) {
        final String sql = "SELECT po.id AS po_id, po.position_column, po.position_row, po.board_id, " +
                "pi.id AS pi_id, pi.type, pi.color, pi.position_id " +
                "FROM position po " +
                "JOIN piece pi ON po.id = pi.position_id " +
                "WHERE board_id=:board_id";

        List<String> keys = List.of("board_id");
        List<Object> values = List.of(boardId);
        SqlParameterSource namedParameters = ParameterSourceCreator.makeParameterSource(keys, values);

        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, namedParameters);

        Map<Position, Piece> result = new HashMap<>();
        for (Map<String, Object> map : maps) {
            result.put(makePosition(map), makePiece(map));
        }
        return result;
    }

    private Position makePosition(Map<String, Object> map) {
        int position_column = (int) map.get("position_column");
        int position_row = (int) map.get("position_row");

        return new Position(Column.findColumn(position_column), Row.findRow(position_row));
    }

    private Piece makePiece(Map<String, Object> map) {
        String type = (String) map.get("type");
        String color = (String) map.get("color");

        return new Piece(Color.findColor(color), Symbol.findSymbol(type).type());
    }

    @Override
    public List<PositionEntity> getPaths(List<PositionEntity> positionEntities, int roomId) {
        List<PositionEntity> realPositions = new ArrayList<>();
        for (PositionEntity positionEntity : positionEntities) {
            realPositions.add(getByColumnAndRowAndBoardId(positionEntity.getColumn(), positionEntity.getRow(), roomId));
        }
        return realPositions;
    }

    private PositionEntity makePosition(ResultSet resultSet, String idLabel) throws SQLException {
        return new PositionEntity(
                resultSet.getInt(idLabel),
                Column.findColumn(resultSet.getInt("position_column")),
                Row.findRow(resultSet.getInt("position_row")),
                resultSet.getInt("board_id"));
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM position";
        jdbcTemplate.update(sql, new MapSqlParameterSource());
    }

}
