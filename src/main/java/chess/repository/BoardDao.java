package chess.repository;

import chess.domain.Chessboard;
import chess.domain.Position;
import chess.dto.MovingPositionDto;
import chess.piece.Piece;
import chess.utils.PieceGenerator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class BoardDao {

    private final JdbcTemplate jdbcTemplate;

    public BoardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Position> positionMapper = (resultSet, rowNum) ->
            new Position(
                    resultSet.getInt("x"),
                    resultSet.getInt("y")
            );

    private final RowMapper<Piece> pieceMapper = (resultSet, rowNum) ->
            PieceGenerator.generate(
                    resultSet.getString("type"),
                    resultSet.getString("color")
            );

    public Chessboard findByGameId(long gameId) {
        final String boardsSql = "SELECT * FROM board WHERE game_id=? ORDER BY position_id ASC";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(boardsSql, gameId);

        Map<Position, Piece> board = new LinkedHashMap<>();
        while (rowSet.next()) {
            board.put(findPositionById(rowSet.getLong("position_id")), findPieceById(rowSet.getLong("piece_id")));
        }

        return new Chessboard(board);
    }

    public void saveBoard(long gameId, Map<Position, Piece> chessboard) {
        final String sql = "INSERT INTO board(game_id,piece_id,position_id) values (" +
                "?," +
                "(SELECT id FROM piece WHERE type=? AND color=?)," +
                "(SELECT id FROM position WHERE x=? AND y=?))";

        for (Position position : chessboard.keySet()) {
            Piece piece = chessboard.get(position);
            jdbcTemplate.update(sql,
                    gameId,
                    piece.getTypeToString(),
                    piece.getColorToString(),
                    position.getX(),
                    position.getY()
            );
        }
    }

    public void saveMove(long gameId, MovingPositionDto movingPosition) {
        long fromPositionId = findPosition(movingPosition.getFromX(), movingPosition.getFromY());
        long fromPieceId = findPieceById(gameId, fromPositionId);

        updatePieceToBlank(gameId, fromPositionId);
        updatePiece(gameId, fromPieceId, findPosition(movingPosition.getToX(), movingPosition.getToY()));
    }

    private Position findPositionById(long id) {
        final String positionSql = "SELECT x,y FROM position WHERE id = ?";
        return jdbcTemplate.queryForObject(positionSql, positionMapper, id);
    }

    private Piece findPieceById(long id) {
        final String pieceSql = "SELECT type,color FROM piece WHERE id = ?";
        return jdbcTemplate.queryForObject(pieceSql, pieceMapper, id);
    }

    private long findPosition(int x, int y) {
        String sql = "SELECT id FROM position WHERE x=? AND y=?";
        return jdbcTemplate.queryForObject(sql, Long.class, x, y);
    }

    private long findPieceById(long gameId, long positionId) {
        String sql = "SELECT piece_id FROM board WHERE game_id=? AND position_id=?";
        return jdbcTemplate.queryForObject(sql, Long.class, gameId, positionId);
    }

    private void updatePieceToBlank(long gameId, long positionId) {
        String sql = "UPDATE board SET piece_id = (SELECT id FROM piece WHERE type='.') WHERE game_id=? AND position_id=?";
        jdbcTemplate.update(sql, gameId, positionId);
    }

    private void updatePiece(long gameId, long pieceId, long positionId) {
        String sql = "UPDATE board SET piece_id=? WHERE game_id=? AND position_id = ?";
        jdbcTemplate.update(sql, pieceId, gameId, positionId);
    }
}
