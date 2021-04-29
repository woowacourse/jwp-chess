package chess.webdao;

import chess.webdto.dao.BoardInfosDto;
import chess.webdto.dao.TeamInfoDto;
import chess.webdto.dao.TurnDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CREATE TABLE board
 * (
 *     board_id       BIGINT      NOT NULL AUTO_INCREMENT,
 *     team           VARCHAR(16) NOT NULL,
 *     position       VARCHAR(16) NOT NULL,
 *     piece          VARCHAR(16) NOT NULL,
 *     is_first_moved BOOLEAN     NOT NULL,
 *     room_id        BIGINT      NOT NULL,
 *     PRIMARY KEY (board_id),
 *     FOREIGN KEY (room_id) REFERENCES room (room_id)
 * );
 */
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
