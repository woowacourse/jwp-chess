package chess.dao;

import chess.domain.game.Position;
import chess.domain.piece.Piece;
import dto.MoveDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import util.PieceConverter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Repository
public class PieceDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Long create(Map<Position, Piece> pieces, String color, Long gameId) {
        String sql = "insert into piece (name, color, position, game_id) values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        for (Position position : pieces.keySet()) {
            jdbcTemplate.update(con -> {
                Piece piece = pieces.get(position);
                PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"team_id"});
                preparedStatement.setString(1, PieceConverter.convertToPieceName(color, piece));
                preparedStatement.setString(2, color);
                preparedStatement.setString(3, position.getKey());
                preparedStatement.setLong(4, gameId);
                return preparedStatement;
            }, keyHolder);
        }

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Map<Position, Piece> load(Long gameId, String color) {
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

    public void delete(final Long gameId, MoveDto moveDto) {
        String sql = "delete from piece where game_id = ? and position = ?";
        jdbcTemplate.update(sql, gameId, moveDto.getTo());
    }

    public void update(final Long gameId, MoveDto moveDto) {
        String sql = "update piece set position = ? where game_id = ? and position = ?";
        jdbcTemplate.update(sql, moveDto.getTo(), gameId, moveDto.getFrom());
    }
}
