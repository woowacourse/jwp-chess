package chess.web.dao;

import chess.domain.room.RoomName;
import chess.domain.room.RoomPassword;
import chess.web.dto.RoomDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

    private final RowMapper<RoomDto> actorRowMapper = (resultSet, rowNum) -> {
        RoomDto roomDto = RoomDto.of(
                Integer.parseInt(resultSet.getString("id")),
                RoomName.of(resultSet.getString("name"))
        );
        return roomDto;
    };

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

    @Override
    public List<RoomDto> findAll() {
        final String sql = "select id, name from room";
        List<RoomDto> roomDtos = jdbcTemplate.query(sql, actorRowMapper);

        return roomDtos;
    }

    @Override
    public void deleteById(int id) {
        final String sql = "delete from room where id = " + id;
        this.jdbcTemplate.update(sql);
    }

    @Override
    public boolean confirmPassword(int id, String password) {
        final String sql = "select password from room where id = " + id;
        final String dbPassword = jdbcTemplate.queryForObject(sql, String.class);
        return passwordEncoder.matches(password, dbPassword);
    }
}
