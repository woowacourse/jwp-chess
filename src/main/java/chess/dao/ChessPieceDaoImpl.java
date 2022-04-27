package chess.dao;

import chess.domain.chesspiece.ChessPiece;
import chess.domain.position.Position;
import chess.dto.ChessPieceMapper;
import chess.dto.response.ChessPieceDto;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChessPieceDaoImpl implements ChessPieceDao {

    private final JdbcTemplate jdbcTemplate;

    public ChessPieceDaoImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ChessPieceDto> findAllByRoomId(final int roomId) {
        final String sql = "SELECT * FROM chess_piece WHERE room_id = ?";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> ChessPieceDto.from(resultSet), roomId);
    }

    @Override
    public int deleteByRoomIdAndPosition(final int roomId, final Position position) {
        final String sql = "DELETE FROM chess_piece WHERE room_id = ? AND position = ?";
        return jdbcTemplate.update(sql, roomId, position.getValue());
    }

    @Override
    public int deleteAllByRoomId(final int roomId) {
        final String sql = "DELETE FROM chess_piece WHERE room_id = ?";
        return jdbcTemplate.update(sql, roomId);
    }

    @Override
    public int saveAll(final int roomId, final Map<Position, ChessPiece> pieceByPosition) {
        String sql = "INSERT INTO chess_piece (room_id, position, chess_piece, color) VALUES (?, ?, ?, ?)";
        final List<Object[]> batchArguments = toBatchArguments(roomId, pieceByPosition);
        final int[] result = jdbcTemplate.batchUpdate(sql, batchArguments);
        return Arrays.stream(result).sum();
    }

    private List<Object[]> toBatchArguments(final int roomId, final Map<Position, ChessPiece> pieceByPosition) {
        return pieceByPosition.entrySet()
                .stream()
                .map(entry -> toBatchArgument(roomId, entry))
                .collect(Collectors.toList());
    }

    private Object[] toBatchArgument(final int roomId, final Entry<Position, ChessPiece> entry) {
        return new Object[]{
                roomId,
                entry.getKey().getValue(),
                ChessPieceMapper.toPieceType(entry.getValue()),
                entry.getValue().color().getValue()
        };
    }

    @Override
    public int updateByRoomIdAndPosition(final int roomId, final Position from, final Position to) {
        final String sql = "UPDATE chess_piece SET position = ? WHERE room_id = ? AND position = ?";
        return jdbcTemplate.update(sql, to.getValue(), roomId, from.getValue());
    }
}
