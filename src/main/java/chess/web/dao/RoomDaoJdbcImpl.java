package chess.web.dao;

import chess.domain.room.RoomName;
import chess.domain.room.RoomPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDaoJdbcImpl implements RoomDao {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RoomDaoJdbcImpl(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public int save(RoomName name, RoomPassword password) {
        String encodePassword = passwordEncoder.encode(password.getPassword());
        final String sql = "insert into room (name, password) values (?, ?)";
        int roomNumber = this.jdbcTemplate.update(
                sql,
                name.toString(),
                encodePassword);

        return roomNumber;
    }
}
