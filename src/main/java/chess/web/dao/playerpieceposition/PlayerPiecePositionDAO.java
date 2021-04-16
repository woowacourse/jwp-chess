package chess.web.dao.playerpieceposition;

import chess.web.dao.entity.GamePiecePositionEntity;
import chess.web.dao.entity.PiecePositionEntity;
import chess.web.domain.piece.Piece;
import chess.web.domain.position.PiecePosition;
import chess.web.domain.position.Position;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PlayerPiecePositionDAO implements PlayerPiecePositionRepository {
    private final JdbcTemplate jdbcTemplate;

    public PlayerPiecePositionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Long playerId, PiecePosition piecePosition) {
        String query = "INSERT INTO player_piece_position (player_id, piece_id, position_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, playerId, piecePosition.getPieceId(), piecePosition.getPositionId());
    }

    @Override
    public Map<Position, Piece> findAllByGameId(Long gameId) {
        String query = "SELECT piece_id, position_id "
            + "FROM player_piece_position "
            + "INNER JOIN "
            + "(SELECT player.id AS player_id FROM player WHERE chess_game_id = ?) "
            + "AS players_id_of_selected_game "
            + "ON player_piece_position.player_id = players_id_of_selected_game.player_id;";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(query, gameId);
        Map<Position, Piece> results = new HashMap<>();
        for (Map<String, Object> gameMap : maps) {
            Piece piece = Piece.of(((BigInteger) gameMap.get("piece_id")).longValue());
            Position position = Position.of(((BigInteger) gameMap.get("position_id")).longValue());
            results.put(position, piece);
        }
        return results;
    }

    @Override
    public List<PiecePositionEntity> findAllByPlayerId(Long playerId) {
        String query =
            "SELECT piece.name AS piece_name, piece.color AS piece_color, position.file_value AS file_value, position.rank_value AS rank_value "
                + "FROM player_piece_position "
                + "INNER JOIN piece ON player_piece_position.piece_id = piece.id "
                + "INNER JOIN position ON player_piece_position.position_id = position.id "
                + "WHERE player_id = ?";
        return jdbcTemplate.query(
            query,
            (resultSet, rowNum) -> new PiecePositionEntity(
                resultSet.getString("piece_name"),
                resultSet.getString("piece_color"),
                resultSet.getString("file_value"),
                resultSet.getString("rank_value")),
            playerId);
    }

    @Override
    public GamePiecePositionEntity findGamePiecePositionByGameIdAndPositionId(Long gameId, Long positionId) {
        String query = "SELECT id, position_id FROM player_piece_position "
            + "INNER JOIN "
            + "(SELECT player.id AS player_id FROM player WHERE chess_game_id = ?) "
            + "AS players "
            + "ON player_piece_position.player_id = players.player_id "
            + "WHERE player_piece_position.position_id = ?;";
        try {
            return jdbcTemplate.queryForObject(
                query,
                (resultSet, rowNum) -> new GamePiecePositionEntity(
                    resultSet.getLong("id"),
                    resultSet.getLong("position_id")),
                gameId, positionId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void updatePiecePosition(GamePiecePositionEntity gamePiecePositionEntity) {
        String query = "UPDATE player_piece_position SET position_id = ? WHERE id = ?";
        jdbcTemplate.update(query, gamePiecePositionEntity.getPositionId(), gamePiecePositionEntity.getPlayerPiecePositionId());
    }

    @Override
    public void removePiecePositionOfGame(GamePiecePositionEntity gamePiecePositionEntity) {
        String query = "DELETE FROM player_piece_position WHERE id = ?";
        jdbcTemplate.update(query, gamePiecePositionEntity.getPlayerPiecePositionId());
    }

    @Override
    public void removeAllByPlayer(Long playerId) {
        String query = "DELETE FROM player_piece_position WHERE player_id = ?";
        jdbcTemplate.update(query, playerId);
    }

    @Override
    public void removeAll() {
        String query = "DELETE FROM player_piece_position";
        jdbcTemplate.update(query);
    }
}
