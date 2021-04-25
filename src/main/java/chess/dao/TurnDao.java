package chess.dao;

import chess.dto.request.TurnChangeRequestDto;
import chess.dto.response.TurnResponseDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class TurnDao {
    private final JdbcTemplate jdbcTemplate;

    public TurnDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void initializeTurn() {
        String query = "INSERT INTO turn (current_turn) VALUE (?)";
        jdbcTemplate.update(query, "white");
    }

    public List<TurnResponseDto> showCurrentTurn() {
        List<TurnResponseDto> turn;
        String query = "SELECT * FROM turn";
        turn = jdbcTemplate.query(
                query, (rs, rowNum) -> new TurnResponseDto(
                        rs.getLong("id"),
                        rs.getString("current_turn"))
        );
        return turn;
    }

    public void changeTurn(final TurnChangeRequestDto turnChangeRequestDto) {
        String query = "UPDATE turn SET current_turn=? WHERE current_turn=?";
        jdbcTemplate.update(query,
                turnChangeRequestDto.getNextTurn(), turnChangeRequestDto.getCurrentTurn());
    }

    public void removeTurn() {
        String query = "DELETE FROM turn";
        jdbcTemplate.update(query);
    }
}
