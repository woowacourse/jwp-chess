package chess.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import chess.model.piece.Piece;
import chess.model.piece.PieceType;
import chess.model.piece.Team;
import chess.model.square.File;
import chess.model.square.Rank;
import chess.model.square.Square;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ChessSquareRepository implements SquareRepository<Square> {

    private final JdbcTemplate jdbcTemplate;

    public ChessSquareRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Square save(Square square) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("square").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("square_file", square.getFile().value());
        parameters.put("square_rank", square.getRank().value());
        parameters.put("board_id", square.getBoardId());

        Number number = simpleJdbcInsert.executeAndReturnKey(parameters);
        return new Square(number.intValue(), square.getFile(), square.getRank(), square.getBoardId());
    }

    @Override
    public Square getBySquareAndBoardId(Square square, int boardId) {
        return jdbcTemplate.queryForObject(
                "SELECT s.id, s.square_file, s.square_rank, s.board_id FROM square AS s " +
                        "WHERE s.square_file = ? AND s.square_rank=? AND s.board_id = ?",
                squareRowMapper(),
                square.getFile().value(), square.getRank().value(), boardId
        );
    }

    @Override
    public int saveAllSquares(int boardId, Set<Square> squares) {
        List<Object[]> batch = changeToObjects(boardId, squares);
        return jdbcTemplate.batchUpdate(
                "INSERT INTO square (square_file, square_rank, board_id) VALUES (?, ?, ?)"
                , batch).length;
    }

    private List<Object[]> changeToObjects(int boardId, Set<Square> squares) {
        return squares.stream()
                .map(square -> new Object[]{square.getFile().value(), square.getRank().value(), boardId})
                .collect(Collectors.toList());
    }

    @Override
    public Map<Square, Piece> findAllSquaresAndPieces(int boardId) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(
                "SELECT po.id AS po_id, po.square_file, po.square_rank, po.board_id, " +
                        "pi.id AS pi_id, pi.type, pi.team, pi.square_id FROM square po " +
                        "INNER JOIN piece pi ON po.id = pi.square_id " +
                        "WHERE board_id=?", boardId);
        Map<Square, Piece> squarePieceMap = new HashMap<>();
        while (sqlRowSet.next()) {
            squarePieceMap.put(makeSquare(sqlRowSet), makePiece(sqlRowSet));
        }
        return squarePieceMap;
    }

    private Piece makePiece(SqlRowSet resultSet) {
        return PieceType.getPiece(resultSet.getInt("pi_id"), resultSet.getString("type"),
                Team.findTeam(resultSet.getString("team")),
                resultSet.getInt("square_id"));
    }

    private Square makeSquare(SqlRowSet resultSet) {
        return new Square(resultSet.getInt("po_id"),
                File.findFileByValue(resultSet.getInt("square_file")),
                Rank.findRank(resultSet.getInt("square_rank")),
                resultSet.getInt("board_id"));
    }

    private RowMapper<Square> squareRowMapper() {
        return (resultSet, rowNum) -> new Square(
                resultSet.getInt("id"),
                File.findFileByValue(resultSet.getInt("square_file")),
                Rank.findRank(resultSet.getInt("square_rank")),
                resultSet.getInt("board_id")
        );
    }
}
