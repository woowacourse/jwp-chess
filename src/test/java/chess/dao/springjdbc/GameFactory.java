package chess.dao.springjdbc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;

public class GameFactory {

    public static void setUpGames(JdbcTemplate jdbcTemplate, String... names) {
        List<Object[]> params = Arrays.stream(names)
                .map(name -> new Object[]{name})
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate("INSERT INTO game SET name = ?, password = 'password'", params);
    }

    private static void setUpBoardsToGames(JdbcTemplate jdbcTemplate, String... names) {
        for (int i = 1; i < names.length+1; i++) {
            jdbcTemplate.update("INSERT INTO piece SET square = 'a1', "
                    + "piece_color = 'white', piece_type = 'pawn', game_id = ?", i);
        }
    }
}
