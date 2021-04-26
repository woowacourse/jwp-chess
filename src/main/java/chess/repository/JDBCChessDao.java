package chess.repository;

import chess.dao.ChessDao;
import chess.domain.piece.Color;
import chess.entity.Chess;
import chess.exception.DuplicateRoomException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JDBCChessDao implements ChessDao {

    static RowMapper<Chess> chessMapper = (rs, rowNum) -> new Chess(
            rs.getString("chess_id"),
            rs.getString("name"),
            Color.findByValue(rs.getString("winner_color")),
            rs.getBoolean("is_running"),
            rs.getTimestamp("created_date").toLocalDateTime()
    );

    private final JdbcTemplate jdbcTemplate;

    public JDBCChessDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Chess chess) {
        if (findByName(chess.getName()).isPresent()) {
            throw new DuplicateRoomException();
        }

        jdbcTemplate.update("INSERT INTO chess VALUES (?, ?, ?, ?, ?)",
                chess.getId(),
                chess.getName(),
                chess.getWinnerColor().getName(),
                chess.isRunning(),
                chess.getCreatedDate()
        );
    }

    @Override
    public Optional<Chess> findByName(String name) {
        List<Chess> chess = jdbcTemplate.query("select * from chess where name = ?",
                chessMapper,
                name
        );
        if (chess.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(chess.get(0));
    }

    @Override
    public void update(Chess chess) {
        jdbcTemplate.update("update chess" +
                        " set is_running = ?, winner_color = ?" +
                        " where chess_id = ? ",
                chess.isRunning(),
                chess.getWinnerColor().getName(),
                chess.getId()
        );
    }

    @Override
    public void deleteByName(String name) {

    }

    @Override
    public List<Chess> findAll() {
        return jdbcTemplate.query("select * from chess order by created_date desc",
                chessMapper);
    }
}

