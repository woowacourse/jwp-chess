package chess.dao;

import chess.domain.TeamColor;
import chess.domain.piece.Piece;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChessDAOJdbc implements ChessDAO {

    private final JdbcTemplate jdbcTemplate;

    public ChessDAOJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void savePieces(Long gameId, List<Piece> pieces) {
        String sql = "insert into game(gameid, name, color, position) values(?,?,?,?)";
        List<Object[]> data = pieces.stream()
            .map(piece -> new Object[]{gameId, piece.name(), piece.color().name(),
                piece.currentPosition().columnAndRow()})
            .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(sql, data);
    }

    @Override
    public Long saveCurrentColor(Long gameId, TeamColor currentColor) {
        String sql = "insert into current_color(game_id, color) values(?,?)";
        jdbcTemplate.update(sql, gameId, currentColor.name());
        return gameId;
    }

    @Override
    public List<ChessDto> chessByGameId(Long gameId) {
        String sql = "select * from game where gameid=?";
        return jdbcTemplate.queryForList(sql, ChessDto.class, gameId);
    }

    @Override
    public Optional<TeamColor> currentTurnByGameId(Long gameId) {
        String sql = "select color from current_color where game_id = ?";
        final List<String> colors = jdbcTemplate.queryForList(sql, String.class, gameId);
        if(colors.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(TeamColor.valueOf(colors.get(0)));
    }

    @Override
    public void deleteAllByGameId(Long gameId) {
        String gameQuery = "delete from game where gameid = ?";
        String colorQuery = "delete from current_color where game_id = ?";

        jdbcTemplate.update(gameQuery, gameId);
        jdbcTemplate.update(colorQuery, gameId);
    }
}
