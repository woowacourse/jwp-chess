package chess.repository;

import chess.model.board.Board;
import chess.model.piece.Piece;
import chess.model.piece.Team;
import chess.model.square.Square;
import chess.model.status.Status;
import chess.model.status.StatusType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ChessBoardRepository implements BoardRepository<Board> {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Board> boardRowMapper = (resultSet, rowNum) -> new Board(
            resultSet.getInt("id"),
            StatusType.findStatus(resultSet.getString("status")),
            Team.findTeam(resultSet.getString("team"))
    );

    public ChessBoardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Board save(Board board) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("board").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("status", board.getStatus().name());
        parameters.put("team", board.getTeam().name());

        Number number = simpleJdbcInsert.executeAndReturnKey(parameters);
        return new Board(number.intValue(), board.getStatus(), board.getTeam());
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update("DELETE FROM board");
    }

    @Override
    public int deleteById(int id) {
        return jdbcTemplate.update("DELETE FROM board WHERE id=?", id);
    }

    @Override
    public Board getById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM board WHERE id=?", boardRowMapper, id);
    }

    public Board test(int id) {
        Map<String, Object> stringObjectMap = jdbcTemplate.queryForMap(
                "SELECT b.id, b.status, b.team, r.title FROM board as b " +
                        "JOIN room as r on r.board_id=b.id WHERE b.id=?", id);

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(
                "SELECT b.id, b.status, b.team, r.title FROM board as b " +
                "JOIN room as r on r.board_id=b.id WHERE b.id=?", id);
//        jdbcTemplate.queryForObject("SELECT * FROM board WHERE id=?", boardRowMapper, id);
        return null;
    }

    @Override
    public Board init(Board board, Map<Square, Piece> startingPieces) {
//        return connectionManager.executeQuery(connection -> {
//            final Board savedBoard = save(board);
//            final ChessSquareRepository chessSquareDao = new ChessSquareRepository(connectionManager);
//            final ChessPieceRepository chessPieceDao = new ChessPieceRepository(connectionManager);
//            chessSquareDao.saveAllSquare(savedBoard.getId());
//            for (Square square : startingPieces.keySet()) {
//                int squareId = chessSquareDao.getSquareIdBySquare(square, savedBoard.getId());
//                chessPieceDao.save(startingPieces.get(square), squareId);
//            }
//            return savedBoard;
//        });
        return null;
    }

    @Override
    public int updateStatus(int boardId, Status status) {
        return jdbcTemplate.update("UPDATE board SET status=? where id=?", status.name(), boardId);
    }

    @Override
    public int updateTeamById(int boardId, Team team) {
        return jdbcTemplate.update("UPDATE board SET team=? where id=?", team.name(), boardId);
    }

    @Override
    public Status getStatusById(int boardId) {
        String status = jdbcTemplate.queryForObject("SELECT status FROM board WHERE id=?", String.class, boardId);
        return StatusType.findStatus(status);
    }
}
