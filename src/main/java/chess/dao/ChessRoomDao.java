package chess.dao;

import chess.dto.GameIdDto;
import chess.dto.RoomAllDto;
import chess.dto.RoomTempDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import chess.domain.Team;
import chess.dto.RoomDto;

import java.util.List;

@Repository
public class RoomDao {

    private final JdbcTemplate jdbcTemplate;

    public RoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void makeGame(Team team, RoomTempDto roomTempDto) {
        final String sql = "insert into room (status, name, password) values(?, ?, ?)";
        jdbcTemplate.update(sql, team.name(), roomTempDto.getName(), roomTempDto.getPassword());
    }

    public List<RoomAllDto> getGames() {
        final String sql = "select * from room";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new RoomAllDto(rs.getLong("id"),
                        Team.valueOf(rs.getString("status")),
                        rs.getString("name"),
                        rs.getString("password")
                ));
    }

    public RoomDto findById(RoomTempDto roomTempDto) {
        final String sql = "select * from room where name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                            new RoomDto(
                                    rs.getLong("id"),
                                    Team.valueOf(rs.getString("status"))
                            ),
                    roomTempDto.getName());
        } catch (Exception e) {
            return null;
        }
    }

    public RoomAllDto findById(GameIdDto gameIdDto) {
        final String sql = "select * from room where id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new RoomAllDto(rs.getLong("id"),
                        Team.valueOf(rs.getString("status")),
                        rs.getString("name"),
                        rs.getString("password")
                ), gameIdDto.getId());
    }

    public void updateStatus(Team team, long roomId) {
        final String sql = "update room set status = ? where id = ?";
        jdbcTemplate.update(sql, team.name(), roomId);
    }

    public void deleteGame(long roomId) {
        final String sql = "delete from room where id = ?";
        jdbcTemplate.update(sql, roomId);
    }
}
