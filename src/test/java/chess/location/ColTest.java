package chess.location;

import org.junit.jupiter.api.Test;
import spring.chess.location.Col;

import static org.assertj.core.api.Assertions.assertThat;

class ColTest {

    @Test
    void is() {
        Col now = Col.of('a');
        Col after = Col.of('b');
        assertThat(now.is(after)).isFalse();
    }

    @Test
    void is2() {
        Col now = Col.of('a');
        Col after = Col.of('a');
        assertThat(now.is(after)).isTrue();
    }
}