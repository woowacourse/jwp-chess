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
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChessPieceDao {

    private final JdbcTemplate jdbcTemplate;

    public ChessPieceDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

    public int saveAll(final String roomName, final Map<Position, ChessPiece> pieceByPosition) {
        String sql = "INSERT INTO chess_piece (room_name, position, chess_piece, color) VALUES (?, ?, ?, ?)";
        final List<Object[]> list = setAllParameter(roomName, pieceByPosition);
        final int[] result = jdbcTemplate.batchUpdate(sql, list);
        return Arrays.stream(result).sum();
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

    public int update(final String roomName, final Position from, final Position to) {
        final String sql = "UPDATE chess_piece SET position = ? WHERE room_name = ? AND position = ?";
        return jdbcTemplate.update(sql, to.getValue(), roomName, from.getValue());
    }
}
