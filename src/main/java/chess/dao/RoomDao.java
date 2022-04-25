package chess.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import chess.domain.Team;
import chess.dto.RoomDto;

@Repository
public class RoomDao {

    private final JdbcTemplate jdbcTemplate;

    public RoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void makeGame(Team team) {
        final String sql = "insert into room (status) values(?)";
        jdbcTemplate.update(sql, team.name());
    }

    public RoomDto findById() {
        final String sql = "select * from room";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                    new RoomDto(
                            rs.getLong("id"),
                            Team.valueOf(rs.getString("status"))
                    ));
        } catch (Exception e) {
            return null;
        }
    }

    public void updateStatus(Team team, long roomId) {
        final String sql = "update room set status = ? where id = ?";
        jdbcTemplate.update(sql, team.name(), roomId);
    }

    public void deleteGame() {
        final String sql = "delete from room";
        jdbcTemplate.update(sql);
    }
}
