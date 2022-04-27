package chess.dao;

import chess.converter.PieceConverter;
import chess.domain.ChessGame;
import chess.domain.piece.Piece;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.domain.state.State;
import chess.dto.ChessGameDto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ChessGameDao {

    private final JdbcTemplate jdbcTemplate;

    public ChessGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(ChessGameDto chessGameDto, String password) {
        String sql = "insert into chessgame (game_name, turn, password) values (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, chessGameDto.getGameName());
            ps.setString(2, chessGameDto.getTurn());
            ps.setString(3, password);
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void update(Long id, ChessGameDto chessGameDto) {
        String sql = "update chessgame set turn = ? where id = ?";
        jdbcTemplate.update(sql, chessGameDto.getTurn(), id);
    }

    public ChessGame findByName(String gameName) {
        String sql = "select CHESSGAME.turn, CHESSGAME.game_name, PIECE.type, PIECE.team, PIECE.`rank`, PIECE.file from CHESSGAME, PIECE\n"
                + "where CHESSGAME.id = PIECE.chessgame_id AND CHESSGAME.game_name = ?;";

        List<ChessGame> result = jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, gameName);
            return preparedStatement;
        }, chessGameRowMapper);

        if(result.isEmpty()) {
            return null;
        }

        return result.get(0);
    }

    public ChessGame findById(Long id) {
        String sql = "select CHESSGAME.turn, CHESSGAME.game_name, PIECE.type, PIECE.team, PIECE.`rank`, PIECE.file from CHESSGAME, PIECE\n"
                + "where CHESSGAME.id = PIECE.chessgame_id AND CHESSGAME.id = ?;";

        List<ChessGame> result = jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setLong(1, id);
            return preparedStatement;
        }, chessGameRowMapper);

        if(result.isEmpty()) {
            return null;
        }

        return result.get(0);
    }

    public List<String> findAllGameName() {
        String sql = "select CHESSGAME.game_name from CHESSGAME";
        return jdbcTemplate.queryForList(sql, String.class);
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

    public void remove(String gameName) {
        String sql = "delete from chessgame where game_name = ?";
        jdbcTemplate.update(sql, gameName);
    }

    public Long findIdByGameNameAndPassword(String gameName, String password) {
        String sql = "select id from chessgame where game_name = ? and password = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, gameName, password);
    }

    public int deleteByGameNameAndPassword(String gameName, String password) {
        String sql = "delete from chessgame where game_name =? and password = ?";
        return jdbcTemplate.update(sql, gameName, password);
    }

    public State findStateByGameNameAndPassword(String gameName, String password) {
        String sql = "select CHESSGAME.turn from CHESSGAME\n"
                + "where CHESSGAME.game_name = ? AND CHESSGAME.password = ?;";

        String turn = jdbcTemplate.queryForObject(sql, String.class, gameName, password);

        return State.getState(turn);
    }
}
