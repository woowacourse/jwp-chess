package wooteco.chess.domain.board;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardFactoryTest {

    @Test
    @DisplayName("Factory Create 테스트")
    void create() {
        assertThat(BoardFactory.create()).isInstanceOf(Board.class);
    }
}
