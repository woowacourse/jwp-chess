package chess.repository;

import chess.domain.ChessGame;
import chess.domain.Chessboard;
import chess.domain.Position;
import chess.piece.Piece;
import chess.utils.PieceGenerator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class ChessboardRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ChessboardRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private final RowMapper<Position> positionMapper = (resultSet, rowNum) ->
            new Position(
                    resultSet.getInt("x"),
                    resultSet.getInt("y")
            );

    private final RowMapper<Piece> pieceMapper = (resultSet, rowNum)
            -> PieceGenerator.generate(
            resultSet.getString("type"),
            resultSet.getString("color")
    );

    public boolean isDataExist() {
        final String sql = "SELECT count(*) FROM games";
        Integer count = namedParameterJdbcTemplate.queryForObject(sql, Map.of(), Integer.class);
        return count != null && count > 0;
    }

    public void save(ChessGame chessGame) {
        truncateAll();
        saveGame(chessGame.getStateToString(), chessGame.getColorOfTurn());
        saveBoard(chessGame.getChessBoard());
    }

    public void truncateAll() {
        final String truncatePieces = "TRUNCATE TABLE boards";
        final String truncateGameInfos = "TRUNCATE TABLE games";

        namedParameterJdbcTemplate.update(truncatePieces, Map.of());
        namedParameterJdbcTemplate.update(truncateGameInfos, Map.of());
    }

    public ChessGame find() {
        final String gameSql = "SELECT * FROM games";
        SqlRowSet rowSet = namedParameterJdbcTemplate.queryForRowSet(gameSql, Map.of());

        rowSet.next();
        return new ChessGame(rowSet.getString("state"), rowSet.getString("turn"), findBoard());
    }

    private Map<Position, Piece> findBoard() {
        final String boardsSql = "SELECT * FROM boards ORDER BY position_id ASC";
        SqlRowSet rowSet = namedParameterJdbcTemplate.queryForRowSet(boardsSql, Map.of());

        Map<Position, Piece> board = new LinkedHashMap<>();
        while (rowSet.next()) {
            board.put(findPositionById(rowSet.getInt("position_id")), findPieceById(rowSet.getInt("piece_id")));
        }

        return board;
    }

    private Position findPositionById(int id) {
        final String positionSql = "SELECT * FROM positions WHERE id = :id";
        return namedParameterJdbcTemplate.queryForObject(positionSql, Map.of("id", id), positionMapper);
    }

    private Piece findPieceById(int id) {
        final String pieceSql = "SELECT * FROM pieces WHERE id = :id";
        return namedParameterJdbcTemplate.queryForObject(pieceSql, Map.of("id", id), pieceMapper);
    }

    private void saveGame(String state, String turn) {
        final String sql = "INSERT INTO games(state,turn) values (:state,:turn)";
        namedParameterJdbcTemplate.update(sql, Map.of("state", state, "turn", turn));
    }

    private void saveBoard(Chessboard chessboard) {
        final String sql = "INSERT INTO boards(piece_id,position_id) values (" +
                "(SELECT id FROM pieces WHERE type=:type AND color=:color)," +
                "(SELECT id FROM positions WHERE x=:x AND y=:y))";

        Map<Position, Piece> board = chessboard.getBoard();

        for (Position position : board.keySet()) {
            Piece piece = board.get(position);
            namedParameterJdbcTemplate.update(sql, Map.of(
                    "x", position.getX(),
                    "y", position.getY(),
                    "type", piece.getTypeToString(),
                    "color", piece.getColorToString())
            );
        }
    }

}
