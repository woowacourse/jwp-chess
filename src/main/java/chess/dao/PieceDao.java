package chess.dao;

import chess.domain.chesspiece.ChessPiece;
import chess.domain.position.Position;
import chess.dto.ChessPieceDto;
import chess.dto.ChessPieceMapper;
import chess.entity.PieceEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class PieceDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;


    public PieceDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("pieces")
                .usingGeneratedKeyColumns("id");
    }

    public void saveAllByRoomId(final List<PieceEntity> pieceEntities) {
        insertActor.executeBatch(SqlParameterSourceUtils.createBatch(pieceEntities));
    }

    public List<PieceEntity> findAllByRoomId(final Long roomId) {
        final String sql = "select * from pieces where room_id = ?";
        return jdbcTemplate.query(sql, rowMapper(), roomId);
    }

    private RowMapper<PieceEntity> rowMapper() {
        return (resultSet, rowNum) -> new PieceEntity(
                resultSet.getLong("id"),
                resultSet.getLong("room_id"),
                resultSet.getString("position"),
                resultSet.getString("type"),
                resultSet.getString("color")
        );
    }

    public void deleteByRoomIdAndPosition(final Long roomId, final String position) {
        final String sql = "delete from pieces where room_id = ? and position = ?";
        jdbcTemplate.update(sql, roomId, position);
    }

    public int saveAll(final String roomName, final Map<Position, ChessPiece> pieceByPosition) {
        String sql = "INSERT INTO chess_piece (room_name, position, chess_piece, color) VALUES (?, ?, ?, ?)";
        final List<Object[]> list = setAllParameter(roomName, pieceByPosition);
        final int[] result = jdbcTemplate.batchUpdate(sql, list);
        return Arrays.stream(result).sum();
    }

    public List<ChessPieceDto> findAllByRoomName(final String roomName) {
        final String sql = "SELECT * FROM chess_piece WHERE room_name = ?";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> ChessPieceDto.from(resultSet), roomName);
    }

    public int deleteByPosition(final String roomName, final Position position) {
        final String sql = "DELETE FROM chess_piece WHERE room_name = ? AND position = ?";
        return jdbcTemplate.update(sql, roomName, position.getValue());
    }

    public int deleteAllByRoomName(final String roomName) {
        final String sql = "DELETE FROM chess_piece WHERE room_name = ?";
        return jdbcTemplate.update(sql, roomName);
    }

    private List<Object[]> setAllParameter(final String roomName, final Map<Position, ChessPiece> pieceByPosition) {
        final List<Object[]> list = new ArrayList<>();
        for (final Entry<Position, ChessPiece> entry : pieceByPosition.entrySet()) {
            final Position position = entry.getKey();
            final ChessPiece chessPiece = entry.getValue();
            Object[] array = {
                    roomName,
                    position.getValue(),
                    ChessPieceMapper.toPieceType(chessPiece),
                    chessPiece.color().getValue()
            };
            list.add(array);
        }
        return list;
    }

    public PieceEntity findByRoomIdPosition(final Long roomId, final String position) {
        final String sql = "select * from pieces where room_id = ? and position = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper(), roomId, position);
    }

    public void updatePositionByRoomId(final Long roomId, final String from, final String to) {
        final String sql = "update pieces set position = ? where room_id = ? and position = ?";
        jdbcTemplate.update(sql, to, roomId, from);
    }

    public int update(final String roomName, final Position from, final Position to) {
        final String sql = "UPDATE chess_piece SET position = ? WHERE room_name = ? AND position = ?";
        return jdbcTemplate.update(sql, to.getValue(), roomName, from.getValue());
    }
}
