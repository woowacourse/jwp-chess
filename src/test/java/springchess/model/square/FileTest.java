package springchess.model.square;

import chess.model.square.File;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileTest {

    @Test
    void availableLocation() {
        assertAll(
                () -> assertThat(chess.model.square.File.D.availableLocation(-3)).isTrue(),
                () -> assertThat(chess.model.square.File.D.availableLocation(4)).isTrue()
        );
    }

    @Test
    void unAvailableLocation() {
        assertAll(
                () -> assertThat(chess.model.square.File.D.availableLocation(-4)).isFalse(),
                () -> assertThat(chess.model.square.File.D.availableLocation(5)).isFalse()
        );
    }

    @Test
    void nextTo() {
        assertAll(
                () -> assertThat(chess.model.square.File.D.nextTo(-3)).isEqualTo(chess.model.square.File.A),
                () -> assertThat(chess.model.square.File.D.nextTo(4)).isEqualTo(chess.model.square.File.H)
        );
    }

    @Test
    void nextToAssert() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> chess.model.square.File.D.nextTo(-4)),
                () -> assertThrows(IllegalArgumentException.class, () -> File.D.nextTo(5))
        );
    }
}
