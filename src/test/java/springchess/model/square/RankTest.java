package springchess.model.square;

import chess.model.square.Rank;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RankTest {

    @Test
    void availableLocation() {
        assertAll(
                () -> assertThat(chess.model.square.Rank.FOUR.availableLocation(-3)).isTrue(),
                () -> assertThat(chess.model.square.Rank.FOUR.availableLocation(4)).isTrue()
        );
    }

    @Test
    void unAvailableLocation() {
        assertAll(
                () -> assertThat(chess.model.square.Rank.FOUR.availableLocation(-4)).isFalse(),
                () -> assertThat(chess.model.square.Rank.FOUR.availableLocation(5)).isFalse()
        );
    }

    @Test
    void nextTo() {
        assertAll(
                () -> assertThat(chess.model.square.Rank.FOUR.nextTo(-3)).isEqualTo(chess.model.square.Rank.ONE),
                () -> assertThat(chess.model.square.Rank.FOUR.nextTo(4)).isEqualTo(chess.model.square.Rank.EIGHT)
        );
    }

    @Test
    void nextToAssert() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> chess.model.square.Rank.FOUR.nextTo(-4)),
                () -> assertThrows(IllegalArgumentException.class, () -> Rank.FOUR.nextTo(5))
        );
    }
}
