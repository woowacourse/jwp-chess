package chess.dao;

import chess.dto.request.TurnChangeRequestDto;
import chess.dto.response.TurnResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TurnDao {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public TurnDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void initializeTurn() {
        String query = "INSERT INTO turn (current_turn) VALUE (?)";
        try {
            jdbcTemplate.update(query, "white");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<TurnResponseDto> showCurrentTurn() {
        List<TurnResponseDto> turn = new ArrayList<>();
        String query = "SELECT * FROM turn";

        try {
            turn = jdbcTemplate.query(
                    query, (rs, rowNum) -> new TurnResponseDto(
                            rs.getLong("id"),
                            rs.getString("current_turn"))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return turn;
    }

    public void changeTurn(final TurnChangeRequestDto turnChangeRequestDto) {
        String query = "UPDATE turn SET current_turn=? WHERE current_turn=?";
        try {
            jdbcTemplate.update(query,
                    turnChangeRequestDto.getNextTurn(), turnChangeRequestDto.getCurrentTurn());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeTurn() {
        String query = "DELETE FROM turn";
        try {
            jdbcTemplate.update(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
