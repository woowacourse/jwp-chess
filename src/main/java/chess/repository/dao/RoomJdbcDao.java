package chess.repository.dao;

import chess.domain.piece.Color;
import chess.repository.entity.RoomEntity;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class RoomJdbcDao implements RoomDao {

    private final JdbcTemplate jdbcTemplate;

    public RoomJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long save(RoomEntity roomEntity) {
        final String sql = "insert into game(room_name, room_password, white_name, black_name) values(?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(getPreparedStatementCreator(roomEntity, sql), keyHolder);

        final Number key = keyHolder.getKey();
        return key.intValue();
    }

    private PreparedStatementCreator getPreparedStatementCreator(RoomEntity roomEntity, String sql) {
        return connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, roomEntity.getName());
            ps.setString(2, roomEntity.getPassword());
            ps.setString(3, roomEntity.getWhite());
            ps.setString(4, roomEntity.getBlack());
            return ps;
        };
    }

    @Override
    public RoomEntity findById(long id) {
        final String sql = "select * from game where id = ?";
        return jdbcTemplate.queryForObject(sql, getRoomEntityRowMapper(), id);
    }

    @Override
    public List<RoomEntity> findAll() {
        final String sql = "select id, room_name, room_password, white_name, black_name, winner, looser, turn, "
                + "case when finished = 1 then 'true' when finished = 0 then 'false' end as finished, "
                + "case when deleted = 1 then 'true' when deleted = 0 then 'false' end as deleted from game where deleted = 0";
        return jdbcTemplate.query(sql, getRoomEntityRowMapper());
    }

    private RowMapper<RoomEntity> getRoomEntityRowMapper() {
        return (resultSet, rowNum) -> {
            return new RoomEntity(
                    resultSet.getLong("id"),
                    resultSet.getString("room_name"),
                    resultSet.getString("room_password"),
                    resultSet.getString("white_name"),
                    resultSet.getString("black_name"),
                    resultSet.getString("winner"),
                    resultSet.getString("looser"),
                    Color.from(resultSet.getString("turn")),
                    resultSet.getBoolean("finished"),
                    resultSet.getBoolean("deleted")
            );
        };
    }

    @Override
    public long deleteById(long id) {
        final String sql = "update game set deleted = 1 where id = ?";
        jdbcTemplate.update(sql, id);
        return id;
    }

    @Override
    public long deleteAll() {
        final String sql = "update game set deleted = 1";
        return jdbcTemplate.update(sql);
    }

    @Override
    public void changeTurnById(long id) {
        final String sql = "update game set turn = "
                + "case when turn = 'black' then 'white' when turn = 'white' then 'black' end where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void finishById(long id) {
        final String sql = "update game set finished = 1 where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
