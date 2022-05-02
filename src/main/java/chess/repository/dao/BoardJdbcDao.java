package chess.repository.dao;

import chess.domain.board.Position;
import chess.domain.piece.PieceFactory;
import chess.dto.MoveRequest;
import chess.repository.entity.BoardEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BoardJdbcDao implements BoardDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BoardJdbcDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<BoardEntity> save(BoardEntity boardEntity) {
        final String sql = "insert into board(game_id, position, piece) "
                + "select ?, initialPosition, initialPiece from initialBoard";

        long gameId = boardEntity.getGameId();
        jdbcTemplate.update(sql, gameId);
        return findById(gameId);
    }

    @Override
    public List<BoardEntity> findById(long id) {
        final String sql = "select * from board where game_id = ?";
        return jdbcTemplate.query(sql, getBoardEntityMapper(), id);
    }

    private RowMapper<BoardEntity> getBoardEntityMapper() {
        return (resultSet, rowNum) -> {
            return new BoardEntity(
                    resultSet.getLong("game_id"),
                    Position.from(resultSet.getString("position")),
                    PieceFactory.getInstance(resultSet.getString("piece"))
            );
        };
    }

    @Override
    public void updateMove(MoveRequest moveRequest) {
        final String delete = "delete from board where game_id = :gameId and position in (:from, :to)";
        namedParameterJdbcTemplate.update(delete, new BeanPropertySqlParameterSource(moveRequest));
        final String insert = "insert into board values (:gameId, :to, :piece)";
        namedParameterJdbcTemplate.update(insert, new BeanPropertySqlParameterSource(moveRequest));
    }

    @Override
    public void deleteById(long id) {
        final String deleteBoardDataSql = "delete from board where game_id = ?";
        jdbcTemplate.update(deleteBoardDataSql, id);
    }

    @Override
    public void deleteAll() {
        final String sql = "delete from board";
        jdbcTemplate.update(sql);
    }
}
