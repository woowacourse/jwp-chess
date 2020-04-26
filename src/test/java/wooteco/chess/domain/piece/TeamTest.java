package wooteco.chess.domain.piece;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamTest {

    @Test
    @DisplayName("Team 생성 테스트")
    void create() {
        assertThat(Team.of("white")).isInstanceOf(Team.class);
    }

    @Test
    @DisplayName("Team 바꾸기 테스트")
    void reverse() {
        assertThat(Team.of("white").reverse()).isEqualTo(Team.BLACK);
    }
}
