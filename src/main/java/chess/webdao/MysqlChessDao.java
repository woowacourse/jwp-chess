package chess.webdao;

import chess.webdto.dao.BoardInfosDto;
import chess.webdto.dao.TeamInfoDto;
import chess.webdto.dao.TurnDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MysqlChessDao implements ChessDao {
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

    @Override
    public void deleteRoomByRoomId(int roomId) {
        final String sql = "DELETE FROM room";
        this.jdbcTemplate.update(sql);
    }

    @Override
    public void deleteBoardByRoomId(int roomId) {
        final String sql = "DELETE FROM board WHERE room_id = (?)";
        this.jdbcTemplate.update(sql, roomId);
    }

    @Override
    public void changeTurnByRoomId(String turn, boolean isPlaying, int roomId) {
        final String sql = "UPDATE room SET turn = (?), is_playing = (?) WHERE room_id = (?)";
        this.jdbcTemplate.update(sql, turn, isPlaying, roomId);
    }

    @Override
    public TurnDto selectTurnByRoomId(long roomId) {
        final String sql = "SELECT * FROM room WHERE room_id = (?)";
        return this.jdbcTemplate.queryForObject(sql, turnMapper, roomId);
    }

    @Override
    public long createRoom(String currentTurn, boolean isPlaying) {
        // 이후, 방 추가용 코드
        //KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO room (turn, is_playing, name, room_id) VALUES (?, ?, ?, ?)";
//        this.jdbcTemplate.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"room_id"});
//            ps.setString(1, currentTurn);
//            ps.setBoolean(2, isPlaying);
//            ps.setString(3, "sdfwer");
//            ps.setLong(4, 1);
//            return ps;
//        }, keyHolder);
//        return keyHolder.getKey().longValue();
        String name = "slwk";
        long roomId = 1;
        return this.jdbcTemplate.update(sql, currentTurn, isPlaying, name, roomId);
    }

    @Override
    public void createBoard(TeamInfoDto teamInfoDto) {
        String sql = "INSERT INTO board (team, position, piece, is_first_moved, room_id) VALUES (?,?,?,?,?)";

        this.jdbcTemplate.update(sql, teamInfoDto.getTeam(), teamInfoDto.getPosition(), teamInfoDto.getPiece(), teamInfoDto.getIsFirstMoved(), teamInfoDto.getRoomId());
    }

    @Override
    public List<BoardInfosDto> selectBoardInfosByRoomId(int roomId) {
        String sql = "SELECT * FROM board WHERE room_id = (?)";
        return this.jdbcTemplate.query(sql, boardInfoMapper, roomId);
    }

}
