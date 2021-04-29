package chess.webdao;

import chess.webdto.dao.BoardInfosDto;
import chess.webdto.dao.TeamInfoDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BoardDao {
    private JdbcTemplate jdbcTemplate;


    private RowMapper<BoardInfosDto> boardInfoMapper = (resultSet, rowNumber) -> {
        BoardInfosDto boardInfosDto = new BoardInfosDto();

        boardInfosDto.setTeam(resultSet.getString("team"));
        boardInfosDto.setPosition(resultSet.getString("position"));
        boardInfosDto.setPiece(resultSet.getString("piece"));
        boardInfosDto.setIsFirstMoved(resultSet.getBoolean("is_first_moved"));

        return boardInfosDto;
    };

    public BoardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deleteBoardByRoomId(long roomId) {
        final String sql = "DELETE FROM board WHERE room_id = (?)";
        this.jdbcTemplate.update(sql, roomId);
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
