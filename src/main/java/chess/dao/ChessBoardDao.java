package chess.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.PieceDto;

@Repository
public class BoardDao {

    private final JdbcTemplate jdbcTemplate;

    public BoardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void savePieces(Map<Position, Piece> board, long roomId) {
        final String sql = "insert into board (position, symbol, room_id) values(?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, board.keySet(), board.size(),
                (rs, position) -> {
                    rs.setString(1, position.getPositionToString());
                    rs.setString(2, board.get(position).getSymbol());
                    rs.setLong(3, roomId);
                });
    }

    public List<PieceDto> findAllPiece(long roomId) {
        String sql = "select * from board where room_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        new PieceDto(
                                rs.getString("position"),
                                rs.getString("symbol")
                        ),
                roomId);
    }

    public void updatePosition(String symbol, String destination, long roomId) {
        final String sql = "update board set symbol = ? where position = ? and room_id = ?";

        jdbcTemplate.update(sql, symbol, destination, roomId);
    }

    public void deleteBoard() {
        final String sql = "delete from board";
        jdbcTemplate.update(sql);
    }
}
