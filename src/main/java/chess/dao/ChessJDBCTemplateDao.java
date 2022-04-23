package chess.dao;

import chess.dto.MoveRequestDto;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChessJDBCTemplateDao implements ChessDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ChessJDBCTemplateDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Map<String, String> getBoardByGameId(String gameId) {
        if (exists(gameId)) {
            return findBoardByGameId(gameId);
        }
        return createNewBoard(gameId);
    }

    private boolean exists(String gameId) {
        final String sql = "select count(1) as isExists from game where id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, gameId) == 1;
    }

    private Map<String, String> findBoardByGameId(String gameId) {
        final String sql = "select position, piece from board where game_id = ?";
        return jdbcTemplate.query(sql, positionsToBoard(), gameId);
    }

    private ResultSetExtractor<Map<String, String>> positionsToBoard() {
        return (ResultSet resultSet) -> {
            Map<String, String> board = new HashMap<>();
            while (resultSet.next()) {
                board.put(resultSet.getString("position"), resultSet.getString("piece"));
            }
            return board;
        };
    }

    private Map<String, String> createNewBoard(String gameId) {
        createGameNumber(gameId);
        insertPiecesOnPositions(gameId);
        return findBoardByGameId(gameId);
    }

    private void createGameNumber(String gameId) {
        final String sql = "insert into game(id) value (?)";
        jdbcTemplate.update(sql, gameId);
    }

    private void insertPiecesOnPositions(String gameId) {
        final String sql = "insert into board(game_id, position, piece) "
                + "select ?, initialPosition, initialPiece from initialBoard";
        jdbcTemplate.update(sql, gameId);
    }

    @Override
    public void move(MoveRequestDto moveRequestDto) {
        deleteFromAndTo(moveRequestDto);
        insertTo(moveRequestDto);
        changeTurn(moveRequestDto.getGameId());
    }

    private void deleteFromAndTo(MoveRequestDto moveRequestDto) {
        final String sql = "delete from board where game_id = :gameId and position in (:from, :to)";
        namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(moveRequestDto));
    }

    private void insertTo(MoveRequestDto moveRequestDto) {
        final String sql = "insert into board values (:gameId, :to, :piece)";
        namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(moveRequestDto));
    }

    @Override
    public String getTurnByGameId(String gameId) {
        final String sql = "select turn from game where id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, gameId);
    }

    private void changeTurn(String gameId) {
        final String sql = "update game set turn = "
                + "case when turn = 'black' then 'white' when turn = 'white' then 'black' end where id = ?";
        jdbcTemplate.update(sql, gameId);
    }
}
