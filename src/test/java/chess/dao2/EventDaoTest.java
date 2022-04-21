package chess.dao2;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.event.Event;
import chess.domain.event.MoveEvent;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class EventDaoTest {

    private static final String TEST_TABLE = "event_test";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private EventDao dao;

    @BeforeEach
    void setUp() {
        dao = new EventDao(namedParameterJdbcTemplate) {
            @Override
            protected String addTable(String sql) {
                return String.format(sql, TEST_TABLE);
            }
        };
        jdbcTemplate.execute("DROP TABLE event_test IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE event_test(game_id  BIGINT NOT NULL, "
                + "type VARCHAR(20) NOT NULL, description VARCHAR(20))");

        jdbcTemplate.execute("INSERT INTO event_test (game_id, type, description) "
                + "VALUES (1, 'MOVE', 'a2 a4'), (1, 'MOVE', 'a7 a5'), (2, 'MOVE', 'a2 a3')");
    }

    @Test
    void findAllByGameId_메서드는_특정_gameId에_해당되는_모든_이벤트를_조회한다() {
        List<Event> actual = dao.findAllByGameId(1);
        List<Event> expected = List.of(new MoveEvent("a2 a4"), new MoveEvent("a7 a5"));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void save_메서드는_새로운_이벤트를_저장한다() {
        dao.save(1, new MoveEvent("b2 b4"));

        List<Event> actual = dao.findAllByGameId(1);

        List<Event> expected = List.of(
                new MoveEvent("a2 a4"), new MoveEvent("a7 a5"), new MoveEvent("b2 b4"));

        assertThat(actual).isEqualTo(expected);
    }
}
