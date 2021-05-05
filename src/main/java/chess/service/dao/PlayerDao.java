package chess.service.dao;

import chess.domain.piece.Owner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class PlayerDao {

    private final JdbcTemplate jdbcTemplate;

    public PlayerDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void enter(final Long roomId, final String playerId) {
        final String query = "UPDATE room SET player2 = ? WHERE id = ?";
        jdbcTemplate.update(query, playerId, roomId);
    }

    public String player1(final Long roomId) {
        final String query = "SELECT player1 FROM room WHERE id = ?";
        return jdbcTemplate.queryForObject(query, String.class, roomId);
    }

    public String player2(final Long roomId) {
        final String query = "SELECT player2 FROM room WHERE id = ?";
        return jdbcTemplate.queryForObject(query, String.class, roomId);
    }

    public List<String> players(final Long roomId) {
        final List<String> players = new ArrayList<>();
        addIfExist(players, player1(roomId));
        addIfExist(players, player2(roomId));
        return players;
    }

    private void addIfExist(final List<String> players, final String playerId) {
        if (Objects.isNull(playerId)) {
            return;
        }
        players.add(playerId);
    }

    public Owner getOwner(final Long roomId, final String playerId) {
        if (isPlayer1(roomId, playerId)) {
            return Owner.WHITE;
        }

        if (isPlayer2(roomId, playerId)) {
            return Owner.BLACK;
        }

        throw new IllegalArgumentException("적절하지 않은 사용자입니다.");
    }

    private boolean isPlayer1(final Long roomId, final String playerId) {
        return playerId.equals(player1(roomId));
    }

    private boolean isPlayer2(final Long roomId, final String playerId) {
        return playerId.equals(player2(roomId));
    }
}
