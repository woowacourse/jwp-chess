package chess.model.domain.piece;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TeamTest {


    @DisplayName("다음 차례 확인")
    @Test
    void nextTurn() {
        Assertions.assertThat(Team.BLACK.next()).isEqualTo(Team.WHITE);
        assertThat(Team.WHITE.next()).isEqualTo(Team.BLACK);
    }

}
