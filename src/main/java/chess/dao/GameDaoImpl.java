package chess.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import chess.dto.response.ChessGameDto;
import chess.entity.Room;

@Repository
public class GameDaoImpl implements GameDao {
    private static final String TABLE_NAME = "game";
    private static final String WHITE_TURN = "WHITE";
    private static final String BLACK_TURN = "BLACK";

    private JdbcTemplate jdbcTemplate;

    public GameDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ChessGameDto getGame(int gameId) {
        String query = String.format("SELECT turn FROM %s WHERE id = ?", TABLE_NAME);
        String turn = jdbcTemplate.queryForObject(query, (resultSet, rowNum) -> resultSet.getString("turn"), gameId);
        return ChessGameDto.of(gameId, turn);
    }

    @Override
    public int createGameAndGetId(String gameName, String gamePassword) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = String.format("INSERT INTO %s(turn, name, password) VALUES ('WHITE', ?, ?)", TABLE_NAME);
        jdbcTemplate.update((Connection conn) -> {
            PreparedStatement pstmt = conn.prepareStatement(query, new String[] {"id"});
            pstmt.setString(1, gameName);
            pstmt.setString(2, gamePassword);
            return pstmt;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public void deleteGame(int gameId) {
        String query = String.format("DELETE FROM %s WHERE id = ?", TABLE_NAME);
        jdbcTemplate.update(query, gameId);
    }

    @Override
    public void updateTurnToWhite(int gameId) {
        updateTurn(gameId, WHITE_TURN);
    }

    @Override
    public void updateTurnToBlack(int gameId) {
        updateTurn(gameId, BLACK_TURN);
    }

    @Override
    public List<Room> inquireAllRooms() {
        String query = String.format("SELECT * FROM %s", TABLE_NAME);
        List<Room> rooms = jdbcTemplate.query(query, (resultSet, rowNum) -> {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String turn = resultSet.getString("turn");
            String password = resultSet.getString("password");
            return new Room(id, turn, name, password);
        });
        return rooms;
    }

    @Override
    public String getPasswordById(int gameId) {
        String query = String.format("SELECT password FROM %s WHERE id = ?", TABLE_NAME);
        return jdbcTemplate.queryForObject(query, (resultSet, rowNum) -> {
            return resultSet.getString("password");
        }, gameId);
    }

    private void updateTurn(int gameId, String turn) {
        String query = String.format("UPDATE %s SET turn = ? WHERE id = ?", TABLE_NAME);
        jdbcTemplate.update(query, turn, gameId);
    }

}
