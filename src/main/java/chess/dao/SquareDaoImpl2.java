package chess.dao;

import chess.model.piece.Piece;
import chess.model.position.Position;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SquareDaoImpl2 {

    private JdbcTemplate jdbcTemplate;

    public SquareDaoImpl2(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Piece> actorRowMapper = (resultSet, rowNum) -> {
        Piece piece = Piece.getPiece(
                resultSet.getString("team") + "_" + resultSet.getString("symbol")
        );
        return piece;
    };

    public void insert(Position position, Piece piece) {
        final String sql = "insert into square (position, team, symbol) values (?, ?, ?)";
        jdbcTemplate.update(sql, position.getKey(), piece.getTeam(), piece.getSymbol());
    }

    public Piece find(Position position) {
        final String sql = "select team, symbol from square where position = ?";
        return jdbcTemplate.queryForObject(sql, actorRowMapper, position.getKey());
    }

    public int delete() {
        final String sql = "delete from square";
        return jdbcTemplate.update(sql);
    }

    public void update(Position position, Piece piece) {
        final String sql = "update square set team = ?, symbol = ? where position = ?";
        jdbcTemplate.update(sql, piece.getTeam(), piece.getSymbol(), position.getKey());
    }
}
