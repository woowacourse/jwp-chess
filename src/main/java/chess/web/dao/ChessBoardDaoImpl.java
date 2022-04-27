package chess.web.dao;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.piece.position.Position;
import chess.web.dto.ChessCellDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ChessBoardDaoImpl implements ChessBoardDao {

    private final JdbcTemplate jdbcTemplate;

    public ChessBoardDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ChessCellDto> actorRowMapper = (resultSet, rowNum) -> {
        ChessCellDto board = new ChessCellDto(
                resultSet.getString("position"),
                resultSet.getString("piece")
        );
        return board;
    };

    @Override
    public void save(Position position, Piece piece, int roomId) {
        final String sql = "insert into board (position, piece, room_id) values (?, ?, ?)";
        this.jdbcTemplate.update(
                sql,
                position.toString(),
                piece.toString(),
                roomId);
    }

    @Override
    public void deleteAll(int roomId) {
        final String sql = "delete from board where room_id = (?)";
        this.jdbcTemplate.update(sql, roomId);
    }

    @Override
    public Map<Position, Piece> findAll(int roomId) {
        final String sql = "select position, piece from board where room_id = (?)";
        List<ChessCellDto> chessBoardDtos = jdbcTemplate.query(sql, actorRowMapper, roomId);

        Map<Position, Piece> board = new HashMap<>();
        for (ChessCellDto boardDto : chessBoardDtos) {
            String position = boardDto.getPosition();
            String piece = boardDto.getPiece();
            board.put(Position.of(position), PieceFactory.of(position, piece));
        }

        return board;
    }
}
