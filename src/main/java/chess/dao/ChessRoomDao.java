package chess.dao;

import chess.dto.GameIdDto;
import chess.dto.MakeRoomDto;
import chess.dto.RoomStatusDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import chess.domain.Team;
import chess.dto.RoomDto;

import java.util.List;

@Repository
public class ChessRoomDao implements RoomDao {

    private final JdbcTemplate jdbcTemplate;

    public ChessRoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void makeGame(Team team, MakeRoomDto makeRoomDto) {
        final String sql = "insert into room (status, name, password) values(?, ?, ?)";
        jdbcTemplate.update(sql, team.name(), makeRoomDto.getName(), makeRoomDto.getPassword());
    }

    @Override
    public List<RoomDto> getGames() {
        final String sql = "select * from room";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new RoomDto(rs.getLong("id"),
                        Team.valueOf(rs.getString("status")),
                        rs.getString("name"),
                        rs.getString("password")
                ));
    }

    @Override
    public RoomStatusDto findById(MakeRoomDto makeRoomDto) {
        final String sql = "select * from room where name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                            new RoomStatusDto(
                                    rs.getLong("id"),
                                    Team.valueOf(rs.getString("status"))
                            ),
                    makeRoomDto.getName());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public RoomDto findById(GameIdDto gameIdDto) {
        final String sql = "select * from room where id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new RoomDto(rs.getLong("id"),
                        Team.valueOf(rs.getString("status")),
                        rs.getString("name"),
                        rs.getString("password")
                ), gameIdDto.getId());
    }

    @Override
    public void updateStatus(Team team, long roomId) {
        final String sql = "update room set status = ? where id = ?";
        jdbcTemplate.update(sql, team.name(), roomId);
    }

    @Override
    public void deleteGame(long roomId) {
        final String sql = "delete from room where id = ?";
        jdbcTemplate.update(sql, roomId);
    }
}
