package chess.dao;

import chess.dto.request.GameIdRequest;
import chess.dto.request.MakeRoomRequest;
import chess.dto.response.RoomStatusResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import chess.domain.Team;
import chess.dto.response.RoomResponse;

import java.util.List;

@Repository
public class ChessRoomDao implements RoomDao {

    private final JdbcTemplate jdbcTemplate;

    public ChessRoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void makeGame(Team team, MakeRoomRequest makeRoomRequest) {
        final String sql = "insert into room (status, name, password) values(?, ?, ?)";
        jdbcTemplate.update(sql, team.name(), makeRoomRequest.getName(), makeRoomRequest.getPassword());
    }

    @Override
    public List<RoomResponse> getGames() {
        final String sql = "select * from room";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new RoomResponse(rs.getLong("id"),
                        Team.valueOf(rs.getString("status")),
                        rs.getString("name"),
                        rs.getString("password")
                ));
    }

    @Override
    public RoomStatusResponse findById(MakeRoomRequest makeRoomRequest) {
        final String sql = "select * from room where name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                            new RoomStatusResponse(
                                    rs.getLong("id"),
                                    Team.valueOf(rs.getString("status"))
                            ),
                    makeRoomRequest.getName());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public RoomResponse findById(GameIdRequest gameIdRequest) {
        final String sql = "select * from room where id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new RoomResponse(rs.getLong("id"),
                        Team.valueOf(rs.getString("status")),
                        rs.getString("name"),
                        rs.getString("password")
                ), gameIdRequest.getId());
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
