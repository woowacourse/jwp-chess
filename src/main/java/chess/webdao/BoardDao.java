package chess.webdao;

import chess.webdto.dao.BoardInfosDto;
import chess.webdto.dao.TeamInfoDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BoardDao {
    private static final String DELETE_BOARD_BY_ROOM_ID = "DELETE FROM board WHERE room_id = (?)";
    private static final String INSERT_BOARD = "INSERT INTO board (team, position, piece, is_first_moved, room_id) VALUES (?,?,?,?,?)";
    private static final String SELECT_BOARD_BY_ROOM_ID = "SELECT * FROM board WHERE room_id = (?)";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<BoardInfosDto> boardInfoMapper = (resultSet, rowNumber) -> {
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
        this.jdbcTemplate.update(DELETE_BOARD_BY_ROOM_ID, roomId);
    }

    public void createBoard(TeamInfoDto teamInfoDto) {
        this.jdbcTemplate.update(INSERT_BOARD, teamInfoDto.getTeam(), teamInfoDto.getPosition(), teamInfoDto.getPiece(), teamInfoDto.getIsFirstMoved(), teamInfoDto.getRoomId());
    }

    public List<BoardInfosDto> selectBoardInfosByRoomId(long roomId) {
        return this.jdbcTemplate.query(SELECT_BOARD_BY_ROOM_ID, boardInfoMapper, roomId);
    }

}
