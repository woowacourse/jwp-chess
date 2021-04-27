package chess.dao;

import chess.domain.entity.Player;
import chess.dto.player.JoinUserDTO;
import chess.exception.InitialSettingDataException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PlayerDAO {
    private final JdbcTemplate jdbcTemplate;

    public PlayerDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int findUserIdByNickname(final String nickname) {
        String query = "SELECT id FROM player WHERE nickname = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, nickname);
    }

    public List<Player> findAll() {
        try {
            String query = "SELECT * from player";
            return jdbcTemplate.query(query, findAllMapper());
        } catch (DataAccessException e) {
            throw new InitialSettingDataException();
        }
    }

    private RowMapper<Player> findAllMapper() {
        return (resultSet, rowNum) -> new Player(
                resultSet.getInt("id"),
                resultSet.getString("nickname")
        );
    }

    public String findNicknameById(final int id) {
        try {
            String query = "SELECT nickname FROM player WHERE id = ?";
            return jdbcTemplate.queryForObject(query, String.class, id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public String findBlackUserByRoomId(final String roomId) {
        String query = "SELECT black.nickname AS black_user " +
                "FROM room JOIN player as black on black.id = room.black_user " +
                "WHERE room.id = ?";
        return findUserByRoomId(query, roomId);
    }

    public String findWhiteUserByRoomId(final String roomId) {
        String query = "SELECT white.nickname AS white_user " +
                "FROM room JOIN player as white on white.id = room.white_user " +
                "WHERE room.id = ?";
        return findUserByRoomId(query, roomId);
    }

    public String findUserByRoomId(final String query, final String roomId) {
        try {
            return jdbcTemplate.queryForObject(query, String.class, roomId);
        } catch (EmptyResultDataAccessException e) {
            return "no User";
        } catch (DataAccessException e) {
            throw new InitialSettingDataException();
        }
    }

    public Player findByPlayerIdAndPassword(final String playerId, final String password) {
        try {
            String query = "SELECT * FROM player WHERE player.nickname = ? AND player.password = ?";
            return jdbcTemplate.queryForObject(query,
                    (rs, rowNum) -> new Player(rs.getInt("id"), rs.getString("nickname")),
                    playerId, password);
        } catch (EmptyResultDataAccessException e) {
            return new Player(-1);
        }
    }

    public int findIdByNicknameAndPassword(final String nickname, final String password) {
        try {
            return jdbcTemplate.queryForObject("SELECT id FROM player WHERE player.nickname = ? AND player.password = ?",
                    Integer.class,
                    nickname, password);
        } catch (DataAccessException e) {
            return 0;
        }
    }

    public Player findByNickname(final String nickname) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM player WHERE nickname = ?",
                    (rs, rowNum) -> new Player(rs.getInt("id"), rs.getString("nickname")), nickname);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int save(final JoinUserDTO joinUserDTO) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("player").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("nickname", joinUserDTO.getNickname());
        parameters.put("password", joinUserDTO.getPassword());

        Number key = jdbcInsert.executeAndReturnKey(parameters);
        return key.intValue();
    }

    public Player findPlayerByIdAndPassword(final String id, final String password) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM player WHERE id = ? AND password = ?",
                    (rs, rowNum) -> new Player(rs.getInt("id"), rs.getString("nickname")), id, password);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Player findById(String id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM player WHERE id = ?",
                    (rs, rowNum) -> new Player(rs.getInt("id"), rs.getString("nickname")), id);
        } catch (EmptyResultDataAccessException e) {
            return new Player(-1);
        }
    }
}
