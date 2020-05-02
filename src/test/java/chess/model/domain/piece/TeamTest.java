package chess.model.domain.piece;

import static org.assertj.core.api.Assertions.assertThat;

import chess.model.domain.piece.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TeamTest {


    @DisplayName("다음 차례 확인")
    @Test
    void nextTurn() {
        Assertions.assertThat(Team.BLACK.nextTurn()).isEqualTo(Team.WHITE);
        assertThat(Team.WHITE.nextTurn()).isEqualTo(Team.BLACK);
    }

}
