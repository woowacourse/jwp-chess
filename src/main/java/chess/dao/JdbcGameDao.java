package chess.dao;

import chess.dao.query.GameQuery;
import chess.service.dto.ChessRequestDto;
import chess.service.dto.GameDto;
import chess.service.dto.GameStatusDto;
import chess.service.dto.RoomResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcGameDao implements GameDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void removeAll(final int id) {
        final String sql = GameQuery.DELETE_GAME.getValue();
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void save(final int id, final ChessRequestDto chessRequestDto) {
        final String sql = GameQuery.INSERT_GAME.getValue();
        jdbcTemplate.update(sql, id,
                chessRequestDto.getTitle(), chessRequestDto.getPassword(),
                chessRequestDto.getTurn(), chessRequestDto.getStatus());
    }

    @Override
    public void modify(final int id, final GameDto gameDto) {
        final String sql = GameQuery.UPDATE_GAME.getValue();
        jdbcTemplate.update(sql, gameDto.getTurn(), gameDto.getStatus(), id);
    }

    @Override
    public void modifyStatus(final int id, final GameStatusDto statusDto) {
        final String sql = GameQuery.UPDATE_GAME_STATUS.getValue();
        jdbcTemplate.update(sql, statusDto.getName(), id);
    }

    @Override
    public GameDto find(final int id) {
        final String sql = GameQuery.SELECT_GAME.getValue();
        try {
            return jdbcTemplate.queryForObject(sql, getGameDtoRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return GameDto.of(null, "ready");
        }
    }

    @Override
    public List<RoomResponseDto> findAll() {
        final String sql = GameQuery.SELECT_ALL_GAME.getValue();
        try {
            return jdbcTemplate.query(sql,
                    (resultSet, rowNum) ->
                            new RoomResponseDto(
                                    resultSet.getInt("game_id"),
                                    resultSet.getString("title"))
            );
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public String findPassword(final int id) {
        final String sql = GameQuery.SELECT_GAME_PASSWORD.getValue();
        try {
            return jdbcTemplate.queryForObject(sql, String.class, id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Optional<Integer> findLastGameId() {
        final String sql = GameQuery.SELECT_LAST_GAME_ID.getValue();
        Optional<Integer> lastGameId = Optional.empty();
        try {
            lastGameId = Optional.ofNullable(jdbcTemplate.queryForObject(sql, Integer.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastGameId;
    }

    private RowMapper<GameDto> getGameDtoRowMapper() {
        return (resultSet, rowNum) ->
                GameDto.of(
                        resultSet.getString("turn"),
                        resultSet.getString("status")
                );
    }
}
