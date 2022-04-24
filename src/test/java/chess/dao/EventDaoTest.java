package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.event.Event;
import chess.domain.event.MoveEvent;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @DisplayName("deleteAllByGameId 메서드로 특정 게임의 모든 이벤트 제거 가능")
    @Nested
    class DeleteAllByGameIdTest {

        @Test
        void gameId에_해당되는_게임의_모든_이벤트를_제거한다() {
            dao.deleteAllByGameId(1);

            boolean exists = dao.findAllByGameId(1).size() > 0;

            assertThat(exists).isFalse();
        }

        @Test
        void gameId에_해당되는_게임의_이벤트가_없으면_예외발생() {
            assertThatThrownBy(() -> dao.deleteAllByGameId(999999))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당되는 이벤트가 없습니다!");
        }
    }
}
