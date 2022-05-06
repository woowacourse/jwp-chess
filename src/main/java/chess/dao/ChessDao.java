package chess.dao;

import chess.domain.Room;
import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.GameRoomDto;
import chess.dto.PieceAndPositionDto;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map.Entry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ChessDao {

    private static final String PIECE_NAME = "piece_name";
    private static final String PIECE_COLOR = "piece_color";
    private static final String POSITION = "position";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<PieceAndPositionDto> pieceAndPositionDtoRowMapper = (resultSet, rowNum) -> new PieceAndPositionDto(
            resultSet.getString(PIECE_NAME),
            resultSet.getString(PIECE_COLOR),
            resultSet.getString(POSITION)
    );

    public ChessDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void updateTurn(final String color, final int gameId) {
        final var sql = "UPDATE game SET current_turn=? WHERE game_id=?";
        jdbcTemplate.update(sql, color, gameId);
    }

    public void deleteAllPiece(final int gameId) {
        final var sql = "DELETE FROM piece WHERE game_id=?";
        jdbcTemplate.update(sql, gameId);
    }

    public void savePiece(final int gameId, final PieceAndPositionDto pieceAndPositionDto) {
        final var sql = "INSERT INTO piece (game_id, piece_name, piece_color, position) VALUES(?,?,?,?)";
        jdbcTemplate.update(sql, gameId, pieceAndPositionDto.getPieceName(), pieceAndPositionDto.getPieceColor(),
                pieceAndPositionDto.getPosition());
    }

    public List<PieceAndPositionDto> findAllPiece(final int gameId) {
        final var sql = "SELECT * FROM piece WHERE game_id=?";
        return jdbcTemplate.query(sql, pieceAndPositionDtoRowMapper, gameId);
    }

    public String findCurrentColor(final int gameId) {
        final var sql = "SELECT current_turn FROM game WHERE game_id=?";
        return jdbcTemplate.queryForObject(sql, String.class, gameId);
    }

    public void deletePiece(final int gameId, final String to) {
        final var sql = "DELETE FROM piece WHERE game_id=? AND position=?";
        jdbcTemplate.update(sql, gameId, to);
    }

    public void updatePiece(final String from, final String to, final int gameId) {
        final var sql = "UPDATE piece SET position=? WHERE position=? AND game_id=?";
        jdbcTemplate.update(sql, to, from, gameId);
    }

    public void deleteGame(final int gameId) {
        var sql = "DELETE FROM game WHERE game_id=?";
        jdbcTemplate.update(sql, gameId);
    }

    public int initGame(String title, String password) {
        String sql = "INSERT INTO game (game_title, game_password) VALUES(?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"game_id"});
            ps.setString(1, title);
            ps.setString(2, password);
            return ps;
        }, keyHolder);
        return getInt(keyHolder);
    }

    private int getInt(KeyHolder keyHolder) {
        try {
            return keyHolder.getKey().intValue();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("게임 생성에 실패 했습니다.");
        }
    }

    public Room findRoomById(final int gameId) {
        final var sql = "SELECT * FROM game WHERE game_id=?";

        return jdbcTemplate.queryForObject(sql, ((rs, rowNum) ->
                new Room(rs.getString("game_title"), rs.getString("game_password"))
        ), gameId);
    }

    public List<GameRoomDto> findAllGame() {
        final var sql = "SELECT game_title, game_id FROM game";
        return jdbcTemplate.query(sql, ((rs, rowNum) ->
                new GameRoomDto(rs.getString("game_title"), rs.getInt("game_id"))
        ));
    }

    public void saveGame(int gameId, Board board) {
        for (Entry<Position, Piece> entry : board.getValue().entrySet()) {
            savePiece(gameId, new PieceAndPositionDto(entry.getKey(), entry.getValue()));
        }
    }

    public void updateTurn(int gameId) {
        final var sql = "UPDATE game SET current_turn = "
                + "CASE WHEN current_turn = 'black' THEN 'white' WHEN current_turn = 'white' THEN 'black' END WHERE game_id = ?";

        jdbcTemplate.update(sql, gameId);
    }
}
