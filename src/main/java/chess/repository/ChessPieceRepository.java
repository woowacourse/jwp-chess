package chess.repository;

import chess.model.piece.Piece;
import chess.model.piece.PieceType;
import chess.model.piece.Team;
import chess.model.square.File;
import chess.model.square.Square;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ChessPieceRepository implements PieceRepository<Piece> {

    private final JdbcTemplate jdbcTemplate;

    public ChessPieceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Piece save(Piece piece, int squareId) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("piece").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("type", piece.name());
        parameters.put("team", piece.team());
        parameters.put("square_id", squareId);

        Number number = simpleJdbcInsert.executeAndReturnKey(parameters);
        return PieceType.getPiece(
                number.intValue(),
                piece.name(),
                piece.team(),
                squareId);
    }

    @Override
    public Piece findBySquareId(int squareId) {
        return jdbcTemplate.queryForObject("SELECT * FROM piece WHERE square_id=?", getRowMapper(), squareId);
    }

    @Override
    public int updatePieceSquareId(int originSquareId, int newSquareId) {
        return jdbcTemplate.update("UPDATE piece SET square_id=? WHERE square_id=?", newSquareId, originSquareId);
    }

    @Override
    public int deletePieceBySquareId(int squareId) {
        return jdbcTemplate.update("DELETE FROM piece WHERE square_id=?", squareId);
    }

    @Override
    public List<Piece> getAllPiecesByBoardId(int boardId) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(
                "SELECT pi.id, pi.type, pi.team, pi.square_id FROM piece pi "
                        + "JOIN square po ON pi.square_id=po.id "
                        + "JOIN board nb ON po.board_id=nb.id WHERE nb.id=?", boardId);
        Map<Square, Piece> squarePieceMap = new HashMap<>();
        List<Piece> pieces = new ArrayList<>();
        while (sqlRowSet.next()) {
            pieces.add(
                    PieceType.getPiece(sqlRowSet.getInt("id"),
                            sqlRowSet.getString("type"),
                            Team.findTeam(sqlRowSet.getString("team")),
                            sqlRowSet.getInt("square_id")
                    ));
        }
        return pieces;
    }

    @Override
    public int countPawnsOnSameFile(int roomId, File file, Team team) {

        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) AS total_count FROM piece pi " +
                        "JOIN square s ON pi.square_id = s.id " +
                        "WHERE s.square_file=? AND s.board_id=? AND pi.type='p' AND pi.team=?",
                Integer.class,
                file.value(), roomId, team.name()
        );
    }

    private RowMapper<Piece> getRowMapper() {
        return (resultSet, rowNum) -> PieceType.getPiece(
                resultSet.getInt("id"),
                resultSet.getString("type"),
                Team.findTeam(resultSet.getString("team")),
                resultSet.getInt("square_id")
        );
    }
}
