package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.event.Event;
import chess.domain.event.MoveEvent;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class EventDaoTest {

    @Autowired
    private EventDao dao;

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
