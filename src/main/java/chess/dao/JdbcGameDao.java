package chess.dao;

import chess.controller.dto.ChessRequestDto;
import chess.domain.piece.PieceColor;
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

@Repository
public class JdbcGameDao implements GameDao {

    private static final int NO_GAME = 0;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void removeAll(final int id) {
        final String sql = "delete from game where game_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void save(final int id, final GameDto gameDto) {
        final String sql = "insert into game (game_id, turn, status) values (?, ?, ?)";
        jdbcTemplate.update(sql, id, gameDto.getTurn(), gameDto.getStatus());
    }

    @Override
    public void save(final int id, final ChessRequestDto chessRequestDto) {
        final String sql = "insert into game (game_id, title, password, turn, status) values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, id,
                chessRequestDto.getTitle(), chessRequestDto.getPassword(),
                chessRequestDto.getTurn(), chessRequestDto.getStatus());
    }

    @Override
    public void modify(final int id, final GameDto gameDto) {
        final String sql = "update game set turn = ?, status = ? where game_id = ?";
        jdbcTemplate.update(sql, gameDto.getTurn(), gameDto.getStatus(), id);
    }

    @Override
    public void modifyStatus(final int id, final GameStatusDto statusDto) {
        final String sql = "update game set status = ? where game_id = ?";
        jdbcTemplate.update(sql, statusDto.getName(), id);
    }

    @Override
    public GameDto find(final int id) {
        final String sql = "select * from game where game_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, getGameDtoRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return GameDto.of(null, "ready");
        }
    }

    @Override
    public List<RoomResponseDto> findAll() {
        final String sql = "select * from game";
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
    public Integer findLastGameId() {
        final String sql = "select game_id from game order by game_id desc limit 1";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
            return NO_GAME;
        }
    }

    private RowMapper<GameDto> getGameDtoRowMapper() {
        return (resultSet, rowNum) ->
                GameDto.of(
                        resultSet.getString("turn"),
                        resultSet.getString("status")
                );
    }
}
