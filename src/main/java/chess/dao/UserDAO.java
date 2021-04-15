package chess.dao;

import chess.dto.UserDTO;
import chess.dto.UsersDTO;
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

    public UsersDTO findByRoomId(final String roomId) {
        String query = "SELECT black.nickname AS black_user, white.nickname AS white_user " +
                "FROM room JOIN user as black on black.id = room.black_user " +
                "JOIN user as white on white.id = room.white_user " +
                "WHERE room.id = ?";
        return jdbcTemplate.queryForObject(query, mapper(), roomId);
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
        String query = "SELECT * from user";
        return jdbcTemplate.query(query, findAllMapper());
    }

    private RowMapper<UserDTO> findAllMapper() {
        return (resultSet, rowNum) -> new UserDTO(
                resultSet.getInt("id"),
                resultSet.getString("nickname")
        );
    }

    public String findNicknameById(final int id) {
        String query = "SELECT nickname FROM user WHERE id = ?";
        return jdbcTemplate.queryForObject(query, String.class, id);
    }
}
