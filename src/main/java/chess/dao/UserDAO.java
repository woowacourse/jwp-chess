package chess.dao;

import chess.dto.user.JoinUserDTO;
import chess.dto.user.UserDTO;
import chess.dto.user.UsersDTO;
import chess.exception.InitialSettingDataException;
import chess.exception.NotEnoughPlayerException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserDAO {
    public static final UserDTO UNKNOWN_USER = new UserDTO();

    private final JdbcTemplate jdbcTemplate;

    public UserDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UsersDTO findUsersByRoomId(final String roomId) {
        try {
            String query = "SELECT black.nickname AS black_user, white.nickname AS white_user " +
                    "FROM room JOIN player as black on black.id = room.black_user " +
                    "JOIN player as white on white.id = room.white_user " +
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
        String query = "SELECT id FROM player WHERE nickname = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, nickname);
    }

    public List<UserDTO> findAll() {
        try {
            String query = "SELECT * from player";
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
            String query = "SELECT nickname FROM player WHERE id = ?";
            return jdbcTemplate.queryForObject(query, String.class, id);
        } catch (DataAccessException e) {
            throw new InitialSettingDataException();
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

    public Optional<UserDTO> findByPlayerIdAndPassword(final String playerId, final String password) {
        try {
            String query = "SELECT * FROM player WHERE player.nickname = ? AND player.password = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(query,
                    (rs, rowNum) -> new UserDTO(rs.getInt("id"), rs.getString("nickname")),
                    playerId, password));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
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

    public Optional<UserDTO> findByNickname(final String nickname) {
        List<UserDTO> users = jdbcTemplate.query("SELECT * FROM player WHERE nickname = ?",
                (rs, rowNum) -> new UserDTO(rs.getInt("id"), rs.getString("nickname")),
                nickname);
        return users.stream().findAny();
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

    public Optional<UserDTO> findPlayerByIdAndPassword(final String id, final String password) {
        List<UserDTO> users = jdbcTemplate.query("SELECT * FROM player WHERE id = ? AND password = ?",
                (rs, rowNum) -> new UserDTO(rs.getInt("id"), rs.getString("nickname")),
                id, password);
        return users.stream().findAny();
    }

    public Optional<UserDTO> findById(String id) {
        List<UserDTO> users = jdbcTemplate.query("SELECT * FROM player WHERE id = ?",
                (rs, rowNum) -> new UserDTO(rs.getInt("id"), rs.getString("nickname")),
                id);
        return users.stream().findAny();
    }
}
