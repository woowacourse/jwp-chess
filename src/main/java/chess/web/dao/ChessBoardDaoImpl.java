package chess.web.dao;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.piece.position.Position;
import chess.web.dto.ChessCellDto;
import chess.web.dto.MoveDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ChessBoardDaoImpl implements ChessBoardDao {

    private final JdbcTemplate jdbcTemplate;

    public ChessBoardDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ChessCellDto> ChessCellMapper = (resultSet, rowNum) -> {
        ChessCellDto board = new ChessCellDto(
                resultSet.getString("position"),
                resultSet.getString("piece")
        );
        return board;
    };

    @Override
    public void save(Position position, Piece piece, int roomId) {
        final String sql = "insert into board (position, piece, room_id) values (?, ?, ?)";
        this.jdbcTemplate.update(
                sql,
                position.toString(),
                piece.toString(),
                roomId);
    }

    @Override
    public Map<Position, Piece> findAllPieces(int roomId) {
        final String sql = "select position, piece from board where room_id = (?)";
        List<ChessCellDto> chessCellDtos = jdbcTemplate.query(sql, ChessCellMapper, roomId);

        Map<Position, Piece> board = new HashMap<>();
        for (ChessCellDto chessCellDto : chessCellDtos) {
            String position = chessCellDto.getPosition();
            String piece = chessCellDto.getPiece();
            board.put(Position.of(position), PieceFactory.of(position, piece));
        }

        return board;
    }

    @Override
    public void movePiece(MoveDto moveDto, int roomId) {
        ChessCellDto sourceCellDto = findByPosition(roomId, moveDto.getSource());

        final String deleteSql = "DELETE FROM board WHERE position = (?) AND room_id = (?);";
        this.jdbcTemplate.update(deleteSql, moveDto.getTarget(), roomId);

        final String updateSql = "UPDATE board SET position = (?), piece = (?) WHERE position = (?) AND room_id = (?);";
        this.jdbcTemplate.update(updateSql, moveDto.getTarget(), sourceCellDto.getPiece(), moveDto.getSource(), roomId);
    }

    @Override
    public ChessCellDto findByPosition(int roomId, String position) {
        final String sql = "SELECT position, piece FROM board WHERE position = (?) AND room_id = (?)";
        return jdbcTemplate.queryForObject(sql, ChessCellMapper, position, roomId);
    }

    @Override
    public boolean boardExistInRoom(int roomId) {
        final String sql = "select count(*) from board where room_id = (?)";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, roomId);
        return count > 0;
    }
}

