package chess.domain.position.cache;

import chess.domain.position.Position;
import chess.domain.position.type.File;
import chess.domain.position.type.Rank;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PositionsCacheTest {

    @DisplayName("체스 칸 위치 캐싱")
    @Test
    void positionsCaching() {
        for (File file : File.values()) {
            assertPositionCachedByFile(file);
        }
    }

    private void assertPositionCachedByFile(File file) {
        for (Rank rank : Rank.values()) {
            Assertions.assertThat(Position.of(file, rank)).isEqualTo(new Position(file, rank));
        }
    }
}