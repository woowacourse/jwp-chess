package chess.repository;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.repository.entity.PieceEntity;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class PieceDaoImpl implements PieceDao {

    private static final String TABLE_NAME = "piece";
    private static final String KEY_NAME = "id";

    private final SimpleJdbcInsert insertActor;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PieceDaoImpl(DataSource dataSource,
        NamedParameterJdbcTemplate jdbcTemplate) {
        this.insertActor = new SimpleJdbcInsert(dataSource)
            .withTableName(TABLE_NAME)
            .usingGeneratedKeyColumns(KEY_NAME);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(int boardId, String target, PieceEntity pieceEntity) {
        Map<String, Object> data = Map.of(
            "board_id", boardId,
            "position", target,
            "color", pieceEntity.getColor(),
            "role", pieceEntity.getRole()
        );
        return insertActor.executeAndReturnKey(data).intValue();
    }

    @Override
    public void saveAll(int boardId, Map<Position, Piece> pieces) {
        List<Map<String, String>> data = pieces.entrySet().stream()
            .map(each -> Map.of(
                "board_id", String.valueOf(boardId),
                "position", each.getKey().name(),
                "color", each.getValue().getColor().name(),
                "role", each.getValue().symbol()
            )).collect(Collectors.toList());
        insertActor.executeBatch(SqlParameterSourceUtils.createBatch(data));
    }

    @Override
    public Optional<PieceEntity> findOne(int boardId, String position) {
        String sql = "select * from piece where board_id = :boardId and position = :position";
        try {
            return Optional.ofNullable(
                jdbcTemplate.queryForObject(sql,
                    Map.of("boardId", boardId, "position", position),
                    getPieceMapper())
            );
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<PieceEntity> findAll(int boardId) {
        String sql = "select * from piece where board_id = :boardId";
        return jdbcTemplate.query(sql, Map.of("boardId", boardId), getPieceMapper());
    }

    private RowMapper<PieceEntity> getPieceMapper() {
        return (resultSet, rowNum) ->
            new PieceEntity(
                resultSet.getString(3),
                resultSet.getString(4),
                resultSet.getString(5)
            );
    }

    @Override
    public void updateOne(int boardId, String afterPosition, PieceEntity pieceEntity) {
        String sql = "update piece set color = :color, role = :role where board_id = :boardId and position = :position";
        jdbcTemplate.update(sql, Map.of(
            "color", pieceEntity.getColor(),
            "role", pieceEntity.getRole(),
            "boardId", boardId,
            "position", afterPosition
        ));
    }

    @Override
    public void deleteOne(int boardId, String position) {
        String sql = "delete from piece where board_id = :boardId and position = :position";
        jdbcTemplate.update(sql, Map.of("boardId", boardId, "position", position));
    }
}
