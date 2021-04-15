package chess.dao.jdbctemplate;

import chess.controller.web.dto.piece.PieceResponseDto;
import chess.dao.PieceDao;
import chess.domain.board.position.Position;
import chess.domain.piece.Piece;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class PieceDaoTemplate implements PieceDao {

    private final JdbcTemplate jdbcTemplate;

    public PieceDaoTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<PieceResponseDto> actorRowMapper = (resultSet, rowNum) ->
            new PieceResponseDto(
                    resultSet.getString("symbol"),
                    resultSet.getString("position")
            );

    @Override
    public long[] savePieces(Long gameId, Map<Position, Piece> pieces) {
        String sql = "INSERT INTO piece(game_id, position, symbol) VALUES (?, ?, ?)";
        List<Object[]> collect = pieces.entrySet()
                .stream()
                .map(entry -> new Object[]{gameId, entry.getKey().parseString(), entry.getValue().getSymbol()})
                .collect(Collectors.toList());
        int[] ints = jdbcTemplate.batchUpdate(sql, collect);
        return Arrays.stream(ints).asLongStream().toArray();
    }

    @Override
    public Long updateSourcePiece(String source, Long gameId) {
        String sql = "UPDATE piece SET symbol = ? WHERE game_id=? && position=?";
        return (long) jdbcTemplate.update(sql, ".", gameId, source);
    }

    @Override
    public Long updateTargetPiece(String target, Piece sourcePiece, Long gameId) {
        String sql = "UPDATE piece SET symbol = ? where game_id = ? && position = ?";
        return (long) jdbcTemplate.update(sql, sourcePiece.getSymbol(), gameId, target);
    }

    @Override
    public List<PieceResponseDto> findPiecesByGameId(Long gameId) {
        String sql = "SELECT * from piece where game_id = ?";
        return jdbcTemplate.query(sql, actorRowMapper, gameId);
    }
}
