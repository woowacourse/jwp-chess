package chess.model.domain.piece;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TeamTest {


    @DisplayName("다음 차례 확인")
    @Test
    void nextTurnIfEmptyMySelf() {
        Assertions.assertThat(Team.BLACK.nextTurnIfEmptyMySelf()).isEqualTo(Team.WHITE);
        assertThat(Team.WHITE.nextTurnIfEmptyMySelf()).isEqualTo(Team.BLACK);
    }

    @DisplayName("이전 차례 확인")
    @Test
    void previousTurnIfEmptyMySelf() {
        assertThat(Team.BLACK.previousTurnIfEmptyMySelf()).isEqualTo(Team.WHITE);
        assertThat(Team.WHITE.previousTurnIfEmptyMySelf()).isEqualTo(Team.BLACK);
    }
}
