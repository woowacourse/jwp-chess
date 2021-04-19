package chess.dao;

import chess.domain.Position;
import chess.domain.piece.Piece;
import dto.MoveDto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import util.PieceConverter;

@Repository
public class PieceDao {
    private final JdbcTemplate jdbcTemplate;

    public PieceDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(Map<Position, Piece> pieces, String color, long gameId) {
        String sql = "insert into piece (name, color, position, game_id) values (?, ?, ?, ?)";
        List<Position> positions = new ArrayList<>(pieces.keySet());
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Piece piece = pieces.get(positions.get(i));
                ps.setString(1, PieceConverter.convertToPieceName(color, piece));
                ps.setString(2, color);
                ps.setString(3, positions.get(i).getKey());
                ps.setLong(4, gameId);
            }

            @Override
            public int getBatchSize() {
                return pieces.size();
            }
        });
    }

    public Map<Position, Piece> load(long gameId, String color) {
        String sql = "select * from piece where game_id = ? and color = ?";

        return jdbcTemplate.query(sql, (ResultSet resultSet) -> {
            Map<Position, Piece> pieces = new HashMap<>();
            while (resultSet.next()) {
                String symbol = resultSet.getString("name");
                String position = resultSet.getString("position");
                Piece piece = PieceConverter.convertToPiece(symbol);
                pieces.put(Position.of(position), piece);
            }
            return pieces;
        }, gameId, color);
    }

    public void delete(final long gameId, MoveDto moveDto) {
        String sql = "delete from piece where game_id = ? and position = ?";
        jdbcTemplate.update(sql, gameId, moveDto.getTo());
    }

    public void update(final long gameId, MoveDto moveDto) {
        String sql = "update piece set position = ? where game_id = ? and position = ?";
        jdbcTemplate.update(sql, moveDto.getTo(), gameId, moveDto.getFrom());
    }
}
