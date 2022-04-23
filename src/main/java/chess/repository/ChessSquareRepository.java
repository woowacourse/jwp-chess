package chess.repository;

import chess.model.room.Room;
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

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                "SELECT s.id, s.square_file, s.square_rank, s.board_id " +
                        "FROM square AS s " +
                        "WHERE s.square_file=? AND s.square_rank=? AND s.board_id=?",
                getRowMapper(),
                square.getFile().value(), square.getRank().value(), boardId
        );
    }

    @Override
    public int saveAllSquare(int boardId) {
        List<Object[]> batch = new ArrayList<>();
        for (File file : File.values()) {
            for (Rank rank : Rank.values()) {
                Object[] values = new Object[]{file.value(), rank.value(), boardId};
                batch.add(values);
            }
        }
        return jdbcTemplate.batchUpdate(
                "INSERT INTO square (square_file, square_rank, board_id) VALUES (?, ?, ?)"
                , batch).length;
    }

    @Override
    public Map<Square, Piece> findAllSquaresAndPieces(int boardId) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(
                "SELECT po.id AS po_id, po.square_file, po.square_rank, po.board_id, " +
                    "pi.id AS pi_id, pi.type, pi.team, pi.square_id " +
                    "FROM square po " +
                    "INNER JOIN piece pi ON po.id = pi.square_id " +
                    "WHERE board_id=?", boardId);
        Map<Square, Piece> squarePieceMap = new HashMap<>();
        while (sqlRowSet.next()) {
            squarePieceMap.put(makeSquare(sqlRowSet), makePiece(sqlRowSet));
        }
        return squarePieceMap;
    }

    private Piece makePiece(SqlRowSet resultSet) {
        return PieceType.getPiece(resultSet.getString("type"),
                resultSet.getInt("pi_id"),
                Team.findTeam(resultSet.getString("team")),
                resultSet.getInt("square_id"));
    }

    private Square makeSquare(SqlRowSet resultSet) {
        return new Square(resultSet.getInt("po_id"),
                File.findFileByValue(resultSet.getInt("square_file")),
                Rank.findRank(resultSet.getInt("square_rank")),
                resultSet.getInt("board_id"));
    }

    @Override
    public List<Square> getPaths(List<Square> squares, int roomId) {
        List<Square> realSquares = new ArrayList<>();
        for (Square square : squares) {
            realSquares.add(getBySquareAndBoardId(square, roomId));
        }
        return realSquares;
    }

    private RowMapper<Square> getRowMapper() {
        return (resultSet, rowNum) -> new Square(
                resultSet.getInt("id"),
                File.findFileByValue(resultSet.getInt("square_file")),
                Rank.findRank(resultSet.getInt("square_rank")),
                resultSet.getInt("board_id")
        );
    }
}
