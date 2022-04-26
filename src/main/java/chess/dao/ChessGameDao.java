package chess.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import chess.converter.PieceConverter;
import chess.domain.ChessGame;
import chess.domain.piece.Piece;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.dto.ChessGameDto;

@Repository
public class ChessGameDao {

    private final JdbcTemplate jdbcTemplate;

    public ChessGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(String gameName, String password) {
        String sql = "insert into chess_game (game_name, password, turn) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
            ps.setString(1, gameName);
            ps.setString(2, password);
            ps.setString(3, "white");
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public List<ChessGameDto> findAllChessGames() {
        String sql = "select id, game_name from chess_game";
        return jdbcTemplate.query(sql, rowMapper());
    }

    private RowMapper<ChessGameDto> rowMapper() {
        return (resultSet, rowNum) -> {
            return new ChessGameDto(
                resultSet.getInt("id"),
                resultSet.getString("game_name")
            );
        };
    }

    public void update(String turn, int chessGameId) {
        String sql = "update chess_game set turn = ? where id = ?";
        jdbcTemplate.update(sql, turn, chessGameId);
    }

    public ChessGame findById(int chessGameId) {
            String sql =
                "select chess_game.turn, chess_game.game_name, piece.type, piece.team, piece.`rank`, piece.file "
                    + "from chess_game, piece "
                    + "where chess_game.id = piece.chess_game_id AND chess_game.id = ?";

            List<ChessGame> result = jdbcTemplate.query(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                preparedStatement.setInt(1, chessGameId);
                return preparedStatement;
            }, chessGameRowMapper);

            if (result.isEmpty()) {
                return null;
            }

            return result.get(0);
    }

    private final RowMapper<ChessGame> chessGameRowMapper = (resultSet, rowNum) -> new ChessGame(
        getTurn(resultSet),
        resultSet.getString("game_name"),
        makeCells(resultSet)
    );

    private String getTurn(ResultSet resultSet) throws SQLException {
        resultSet.beforeFirst();
        resultSet.next();
        return resultSet.getString("turn");
    }

    private Map<Position, Piece> makeCells(ResultSet resultSet) throws SQLException {
        resultSet.beforeFirst();

        Map<Position, Piece> cells = new HashMap<>();

        while (resultSet.next()) {
            Position position = makePosition(resultSet);
            Piece piece = makePiece(resultSet);
            cells.put(position, piece);
        }

        return cells;
    }

    private Position makePosition(ResultSet resultSet) throws SQLException {
        int rank = resultSet.getInt("rank");
        String file = resultSet.getString("file");

        return Position.of(File.toFile(file.charAt(0)), Rank.toRank(rank));
    }

    private Piece makePiece(ResultSet resultSet) throws SQLException {
        String type = resultSet.getString("type");
        String team = resultSet.getString("team");

        return PieceConverter.from(type, team);
    }

    public void delete(int chessGameId) {
        String sql = "delete from chess_game where id = ?";
        jdbcTemplate.update(sql, chessGameId);
    }
}
