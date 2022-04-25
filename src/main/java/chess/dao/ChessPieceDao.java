package chess.dao;

import chess.domain.chesspiece.ChessPiece;
import chess.domain.position.Position;
import chess.dto.ChessPieceDto;
import chess.dto.ChessPieceMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChessPieceDao {

    private final JdbcTemplate jdbcTemplate;

    public ChessPieceDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ChessPieceDto> findAllByRoomId(final int roomId) {
        final String sql = "SELECT * FROM chess_piece WHERE room_id = ?";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> ChessPieceDto.from(resultSet), roomId);
    }

    public int deleteByRoomIdAndPosition(final int roomId, final Position position) {
        final String sql = "DELETE FROM chess_piece WHERE room_id = ? AND position = ?";
        return jdbcTemplate.update(sql, roomId, position.getValue());
    }

    public int deleteAllByRoomId(final int roomId) {
        final String sql = "DELETE FROM chess_piece WHERE room_id = ?";
        return jdbcTemplate.update(sql, roomId);
    }

    public int saveAll(final int roomId, final Map<Position, ChessPiece> pieceByPosition) {
        String sql = "INSERT INTO chess_piece (room_id, position, chess_piece, color) VALUES (?, ?, ?, ?)";
        final List<Object[]> list = setAllParameter(roomId, pieceByPosition);
        final int[] result = jdbcTemplate.batchUpdate(sql, list);
        return Arrays.stream(result).sum();
    }

    private List<Object[]> setAllParameter(final int roomId, final Map<Position, ChessPiece> pieceByPosition) {
        final List<Object[]> list = new ArrayList<>();
        for (final Entry<Position, ChessPiece> entry : pieceByPosition.entrySet()) {
            final Position position = entry.getKey();
            final ChessPiece chessPiece = entry.getValue();
            Object[] array = {
                    roomId,
                    position.getValue(),
                    ChessPieceMapper.toPieceType(chessPiece),
                    chessPiece.color().getValue()
            };
            list.add(array);
        }
        return list;
    }

    public int updateByRoomIdAndPosition(final int roomId, final Position from, final Position to) {
        final String sql = "UPDATE chess_piece SET position = ? WHERE room_id = ? AND position = ?";
        return jdbcTemplate.update(sql, to.getValue(), roomId, from.getValue());
    }
}
