package chess.dao;

import chess.dto.user.UserDTO;
import chess.dto.user.UsersDTO;
import chess.exception.InitialSettingDataException;
import chess.exception.NotEnoughPlayerException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;

    public UserDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UsersDTO findUsersByRoomId(final String roomId) {
        try {
            String query = "SELECT black.nickname AS black_user, white.nickname AS white_user " +
                    "FROM room JOIN user as black on black.id = room.black_user " +
                    "JOIN user as white on white.id = room.white_user " +
                    "WHERE room.id = ?";
            return jdbcTemplate.queryForObject(query, mapper(), roomId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotEnoughPlayerException(roomId);
        }
    }

    private RowMapper<UsersDTO> mapper() {
        return (resultSet, rowNum) -> new UsersDTO(
                resultSet.getString("black_user"),
                resultSet.getString("white_user")
        );
    }

    public int findUserIdByNickname(final String nickname) {
        String query = "SELECT id FROM user WHERE nickname = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, nickname);
    }

    public List<UserDTO> findAll() {
        try {
            String query = "SELECT * from user";
            return jdbcTemplate.query(query, findAllMapper());
        } catch (DataAccessException e) {
            throw new InitialSettingDataException();
        }
    }

    private RowMapper<UserDTO> findAllMapper() {
        return (resultSet, rowNum) -> new UserDTO(
                resultSet.getInt("id"),
                resultSet.getString("nickname")
        );
    }

    public String findNicknameById(final int id) {
        try {
            String query = "SELECT nickname FROM user WHERE id = ?";
            return jdbcTemplate.queryForObject(query, String.class, id);
        } catch (DataAccessException e) {
            throw new InitialSettingDataException();
        }
    }

    public String findBlackUserByRoomId(final String roomId) {
        String query = "SELECT black.nickname AS black_user " +
                "FROM room JOIN user as black on black.id = room.black_user " +
                "WHERE room.id = ?";
        return findUserByRoomId(query, roomId);
    }

    public String findWhiteUserByRoomId(final String roomId) {
        String query = "SELECT white.nickname AS white_user " +
                "FROM room JOIN user as white on white.id = room.white_user " +
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

    public UserDTO createUser(final String playerId, final String password) {
        String query = "INSERT INTO user (nickname, password) values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(query, new String[]{"id"});
            ps.setString(1, playerId);
            ps.setString(2, password);
            return ps;
        }, keyHolder);
        return new UserDTO(Objects.requireNonNull(keyHolder.getKey()).intValue(), playerId);
    }

    public Optional<UserDTO> findByPlayerIdAndPassword(final String playerId, final String password) {
        try {
            String query = "SELECT * FROM user WHERE user.nickname = ? AND user.password = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(query,
                    (rs, rowNum) -> new UserDTO(rs.getInt("id"), rs.getString("nickname")),
                    playerId, password));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
