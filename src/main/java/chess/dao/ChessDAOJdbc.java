package chess.dao;

import chess.domain.ChessGame;
import chess.domain.ChessGameImpl;
import chess.domain.PieceForm;
import chess.domain.Pieces;
import chess.domain.Position;
import chess.domain.TeamColor;
import chess.domain.converter.StringPositionConverter;
import chess.domain.piece.Piece;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChessDAOJdbc implements ChessDAO {

    private final JdbcTemplate jdbcTemplate;
    private final StringPositionConverter converter;

    public ChessDAOJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        converter = new StringPositionConverter();
    }


    @Override
    public Long saveGame(ChessGame chessGame, Long gameId) {
        deleteAllByGameId(gameId);
        saveCurrentColor(gameId, chessGame.currentColor());
        savePieces(gameId, chessGame.pieces().asList());
        return gameId;
    }

    private void savePieces(Long gameId, List<Piece> pieces) {
        String sql = "insert into game(gameid, name, color, position) values(?,?,?,?)";
        List<Object[]> data = pieces.stream()
            .map(piece -> new Object[]{gameId, piece.name(), piece.color(),
                piece.currentPosition().columnAndRow()})
            .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(sql, data);
    }

    private void saveCurrentColor(Long gameId, TeamColor currentColor) {
        String sql = "insert into current_color(game_id, color) values(?,?)";
        jdbcTemplate.update(sql, gameId, currentColor);
    }

    @Override
    public Optional<ChessGame> loadGame(Long gameId) {
        final List<ChessDto> gameById = findGameById(gameId);
        final List<String> color = currentColorById(gameId);

        if (gameById.isEmpty() || color.isEmpty()) {
            return Optional.empty();
        }

        List<Piece> pieces = gameById.stream()
            .map(result -> {
                Position position = converter.convert(result.getPosition());
                TeamColor teamColor = TeamColor.valueOf(result.getColor());
                return PieceForm.create(result.getName(), teamColor, position);
            }).collect(Collectors.toList());

        return Optional
            .of(ChessGameImpl.from(Pieces.from(pieces), TeamColor.teamColor(color.get(0))));
    }

    private List<ChessDto> findGameById(Long gameId) {
        String sql = "select * from game where gameid=?";
        return jdbcTemplate.queryForList(sql, ChessDto.class, gameId);
    }

    private List<String> currentColorById(Long gameId) {
        String sql = "select color from current_color where game_id = ?";
        return jdbcTemplate.queryForList(sql, String.class, gameId);
    }

    @Override
    public void removeGame(Long gameId) {
        deleteAllByGameId(gameId);
    }

    private void deleteAllByGameId(Long gameId) {
        String gameQuery = "delete from game where gameid = ?";
        String colorQuery = "delete from current_color where game_id = ?";

        jdbcTemplate.update(gameQuery, gameId);
        jdbcTemplate.update(colorQuery, gameId);
    }
}
