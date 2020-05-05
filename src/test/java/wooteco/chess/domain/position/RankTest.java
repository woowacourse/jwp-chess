package wooteco.chess.domain.position;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class RankTest {

    @Test
    void getRankDifferenceTest() {
        assertThat(Rank.EIGHT.getRankDifference(Rank.FIVE)).isEqualTo(-3);
    }
}
