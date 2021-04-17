package chess.dao;

import chess.domain.game.ChessGameEntity;
import chess.dto.ChessGameStatusDto;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class ChessGameDAO {

    private final JdbcTemplate jdbcTemplate;

    public ChessGameDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<ChessGameEntity> findByStateIsBlackTurnOrWhiteTurn() {
        String query = "SELECT * FROM chess_game WHERE state in(?, ?)";
        List<ChessGameEntity> chessGameEntities = jdbcTemplate.query(query
                , (rs, rowNum) -> new ChessGameEntity(rs.getLong("id"), rs.getString("state"))
                , "BlackTurn", "WhiteTurn");
        if (chessGameEntities.isEmpty()) {
            return Optional.empty();
        }

        ChessGameEntity chessGameEntity = DataAccessUtils.nullableSingleResult(chessGameEntities);
        return Optional.of(chessGameEntity);
    }

    public Long save() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO chess_game(state) VALUES(?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, "BlackTurn");
            return preparedStatement;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public void updateState(final Long id, final String state) {
        String query = "UPDATE chess_game SET state = ? WHERE id = ?";
        jdbcTemplate.update(query, state, id);
    }

    public ChessGameStatusDto findIsExistPlayingChessGameStatus() {
        String query = "SELECT * FROM chess_game WHERE state in(?, ?)";
        List<ChessGameEntity> chessGameEntities = jdbcTemplate.query(query
                , (rs, rowNum) -> new ChessGameEntity(rs.getLong("id"), rs.getString("state"))
                , "BlackTurn", "WhiteTurn");
        if (chessGameEntities.isEmpty()) {
            return ChessGameStatusDto.isNotExist();
        }

        return ChessGameStatusDto.exist();
    }

}
