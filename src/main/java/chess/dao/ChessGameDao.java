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
            ps.setString(3, "ready");
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public List<ChessGameDto> findAllChessGames() {
        String sql = "select id, game_name, turn from chess_game limit 100";
        return jdbcTemplate.query(sql, chessGameDtoMapper());
    }

    public void update(String turn, int chessGameId) {
        String sql = "update chess_game set turn = ? where id = ?";
        jdbcTemplate.update(sql, turn, chessGameId);
    }

    private RowMapper<ChessGameDto> chessGameDtoMapper() {
        return (resultSet, rowNum) -> {
            return new ChessGameDto(
                resultSet.getInt("id"),
                resultSet.getString("game_name"),
                resultSet.getString("turn")
            );
        };
    }

    public ChessGame findById(int chessGameId) {
        String sql =
            "select chess_game.turn, chess_game.game_name, chess_game.password, piece.type, piece.team, piece.`rank`, piece.file "
                + "from chess_game, piece "
                + "where chess_game.id = piece.chess_game_id AND chess_game.id = ?";
        return jdbcTemplate.queryForObject(sql, chessGameRowMapper, chessGameId);
    }

    private final RowMapper<ChessGame> chessGameRowMapper = (resultSet, rowNum) -> initChessGame(resultSet);

    private ChessGame initChessGame(ResultSet resultSet) throws SQLException {
        Map<Position, Piece> cells = new HashMap<>();
        cells.put(makePosition(resultSet), makePiece(resultSet));
        String turn = resultSet.getString("turn");
        String gameName = resultSet.getString("game_name");
        String password = resultSet.getString("password");
        while (resultSet.next()) {
            Position position = makePosition(resultSet);
            Piece piece = makePiece(resultSet);
            cells.put(position, piece);
        }

        return new ChessGame(turn, gameName, password, cells);
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
