package chess.dao;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.board.Position;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.dto.GameCreateRequest;
import chess.dto.GameDeleteResponse;
import chess.dto.GameDto;
import chess.dto.MoveRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class JDBCTemplateChessDao implements ChessDao {
    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public JDBCTemplateChessDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public int create(GameCreateRequest request) {
        final String sql = "insert into game(room_name, room_password, white_name, black_name) values(?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(getPreparedStatementCreator(request, sql), keyHolder);
        final Number key = keyHolder.getKey();

        final int id = key.intValue();
        insertPiecesOnPositions(id);
        return id;
    }

    private PreparedStatementCreator getPreparedStatementCreator(GameCreateRequest request, String sql) {
        return connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, request.getRoomName());
            ps.setString(2, passwordEncoder.encode(request.getRoomPassword()));
            ps.setString(3, request.getWhiteName());
            ps.setString(4, request.getBlackName());
            return ps;
        };
    }

    private void insertPiecesOnPositions(int gameId) {
        final String sql = "insert into board(game_id, position, piece) "
                + "select ?, initialPosition, initialPiece from initialBoard";
        jdbcTemplate.update(sql, gameId);
    }

    @Override
    public List<GameDto> findAll() {
        final String sql = "select id, room_name, white_name, black_name, "
                + "case when finished = 1 then 'true' when finished = 0 then 'false' end as finished "
                + "from game where deleted = 0";
        return jdbcTemplate.query(sql, getGameRoomDtoRowMapper());
    }

    private RowMapper<GameDto> getGameRoomDtoRowMapper() {
        return (resultSet, rowNum) -> {
            return new GameDto(resultSet.getInt("id"),
                    resultSet.getString("room_name"),
                    resultSet.getString("white_name"),
                    resultSet.getString("black_name"),
                    resultSet.getBoolean("finished"));
        };
    }

    @Override
    public GameDeleteResponse deleteById(int id) {
        final String changeFlagSql = "update game set deleted = 1 where id = ?";
        jdbcTemplate.update(changeFlagSql, id);
        final String deleteBoardDataSql = "delete from board where game_id = ?";
        jdbcTemplate.update(deleteBoardDataSql, id);
        return new GameDeleteResponse(true, String.valueOf(id));
    }

    @Override
    public Board findBoardByGameId(int gameId) {
        final String sql = "select position, piece from board where game_id = ?";
        final Map<Position, Piece> board = jdbcTemplate.query(sql, positionsToBoard(), gameId);
        final Color color = findTurnByGameId(gameId);
        return BoardFactory.newInstance(board, color);
    }

    private Color findTurnByGameId(int gameId) {
        final String sql = "select turn from game where id = ?";
        return Color.from(jdbcTemplate.queryForObject(sql, String.class, gameId));
    }

    private ResultSetExtractor<Map<Position, Piece>> positionsToBoard() {
        return (ResultSet resultSet) -> {
            Map<Position, Piece> board = new HashMap<>();
            while (resultSet.next()) {
                board.put(Position.from(resultSet.getString("position")),
                        PieceFactory.getInstance(resultSet.getString("piece")));
            }
            return board;
        };
    }

    @Override
    public void updateBoardByMove(MoveRequest moveRequest) {
        deleteFromAndTo(moveRequest);
        insertTo(moveRequest);
    }

    private void deleteFromAndTo(MoveRequest moveRequest) {
        final String sql = "delete from board where game_id = :gameId and position in (:from, :to)";
        namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(moveRequest));
    }

    private void insertTo(MoveRequest moveRequest) {
        final String sql = "insert into board values (:gameId, :to, :piece)";
        namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(moveRequest));
    }

    @Override
    public void finishBoardById(int gameId) {
        final String sql = "update game set finished = 1 where id = ?";
        jdbcTemplate.update(sql, gameId);
    }

    @Override
    public GameDto findById(int id) {
        final String sql = "select id, room_name, white_name, black_name, "
                + "case when finished = 1 then 'true' when finished = 0 then 'false' end as finished "
                + "from game where id = ?";
        return jdbcTemplate.queryForObject(sql, getGameRoomDtoRowMapper(), id);
    }

    @Override
    public void changeTurnByGameId(int gameId) {
        final String sql = "update game set turn = "
                + "case when turn = 'black' then 'white' when turn = 'white' then 'black' end where id = ?";
        jdbcTemplate.update(sql, gameId);
    }

    @Override
    public String findPasswordById(int id) {
        final String sql = "select room_password from game where id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, id);
    }
}
