package chess.dao;

import chess.dto.UserDTO;
import chess.dto.UsersDTO;
import chess.exception.InitialSettingDataException;
import chess.exception.NotEnoughPlayerException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
