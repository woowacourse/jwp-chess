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
        jdbcTemplate.batchUpdate("INSERT INTO game SET name = ?", params);
    }
}
