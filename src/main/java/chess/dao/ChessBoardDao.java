package chess.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.response.PieceResponse;

@Repository
public class ChessBoardDao implements BoardDao {

    private final JdbcTemplate jdbcTemplate;

    public ChessBoardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void savePieces(Map<Position, Piece> board, long roomId) {
        final String sql = "insert into board (position, symbol, room_id) values(?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, board.keySet(), board.size(),
                (rs, position) -> {
                    rs.setString(1, position.getPositionToString());
                    rs.setString(2, board.get(position).getSymbol());
                    rs.setLong(3, roomId);
                });
    }

    @Override
    public List<PieceResponse> findAllPiece(long roomId) {
        String sql = "select * from board where room_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        new PieceResponse(
                                rs.getString("position"),
                                rs.getString("symbol")
                        ),
                roomId);
    }

    @Override
    public void updatePosition(String symbol, String destination, long roomId) {
        final String sql = "update board set symbol = ? where position = ? and room_id = ?";
        jdbcTemplate.update(sql, symbol, destination, roomId);
    }

    @Override
    public void deleteBoard(long roomId) {
        final String sql = "delete from board where room_id = ?";
        jdbcTemplate.update(sql, roomId);
    }
}