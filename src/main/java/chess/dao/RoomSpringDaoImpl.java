package chess.dao;

import chess.domain.Team;
import chess.dto.RoomDto;
import org.springframework.jdbc.core.JdbcTemplate;

public class RoomSpringDaoImpl implements RoomDao{
    private JdbcTemplate jdbcTemplate;

    public RoomSpringDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public RoomDto findById(long roomId) {
        final String sql = "select * from room  where id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new RoomDto(
                        rs.getLong("id"),
                        Team.valueOf(rs.getString("status"))
                ), roomId);
    }
    @Override
    public void delete(long roomId) {
        final String sql = "delete from room where id = ?";
        jdbcTemplate.update(sql, roomId);
    }
    @Override
    public void save(long roomId, Team team) {
        final String sql = "insert into room (id, status) values(?, ?)";
        jdbcTemplate.update(sql, roomId, team.name());
    }
    @Override
    public void updateStatus(Team team, long roomId) {
        final String sql = "update room set status = ? where id = ?";
        jdbcTemplate.update(sql, team.name(), roomId);
    }
}
