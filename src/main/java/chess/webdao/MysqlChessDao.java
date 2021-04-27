package chess.webdao;

import chess.webdto.dao.BoardInfosDto;
import chess.webdto.dao.TeamInfoDto;
import chess.webdto.dao.TurnDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MysqlChessDao {
    private JdbcTemplate jdbcTemplate;

    private RowMapper<TurnDto> turnMapper = (resultSet, rowNum) -> {
        TurnDto turnDto = new TurnDto();

        turnDto.setTurn(resultSet.getString("turn"));
        turnDto.setIsPlaying(resultSet.getBoolean("is_playing"));

        return turnDto;
    };

    private RowMapper<BoardInfosDto> boardInfoMapper = (resultSet, rowNumber) -> {
        BoardInfosDto boardInfosDto = new BoardInfosDto();

        boardInfosDto.setTeam(resultSet.getString("team"));
        boardInfosDto.setPosition(resultSet.getString("position"));
        boardInfosDto.setPiece(resultSet.getString("piece"));
        boardInfosDto.setIsFirstMoved(resultSet.getBoolean("is_first_moved"));

        return boardInfosDto;
    };

    public MysqlChessDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deleteRoomByRoomId(long roomId) {
        final String sql = "DELETE FROM room";
        this.jdbcTemplate.update(sql);
    }

    public void deleteBoardByRoomId(long roomId) {
        final String sql = "DELETE FROM board WHERE room_id = (?)";
        this.jdbcTemplate.update(sql, roomId);
    }

    public void changeTurnByRoomId(String turn, boolean isPlaying, long roomId) {
        final String sql = "UPDATE room SET turn = (?), is_playing = (?) WHERE room_id = (?)";
        this.jdbcTemplate.update(sql, turn, isPlaying, roomId);
    }

    public TurnDto selectTurnByRoomId(long roomId) {
        final String sql = "SELECT * FROM room WHERE room_id = (?)";
        return this.jdbcTemplate.queryForObject(sql, turnMapper, roomId);
    }

    public long createRoom(String currentTurn, boolean isPlaying) {
        String sql = "INSERT INTO room (turn, is_playing, name, room_id) VALUES (?, ?, ?, ?)";
        String name = "한글도 되나";
        long roomId = 1;
        return this.jdbcTemplate.update(sql, currentTurn, isPlaying, name, roomId);
    }

    public void createBoard(TeamInfoDto teamInfoDto) {
        String sql = "INSERT INTO board (team, position, piece, is_first_moved, room_id) VALUES (?,?,?,?,?)";
        this.jdbcTemplate.update(sql, teamInfoDto.getTeam(), teamInfoDto.getPosition(), teamInfoDto.getPiece(), teamInfoDto.getIsFirstMoved(), teamInfoDto.getRoomId());
    }

    public List<BoardInfosDto> selectBoardInfosByRoomId(long roomId) {
        String sql = "SELECT * FROM board WHERE room_id = (?)";
        return this.jdbcTemplate.query(sql, boardInfoMapper, roomId);
    }

}
