package chess.model;

import chess.model.piece.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamTest {

    @ParameterizedTest
    @DisplayName("진영의 색깔이 검은색인지 확인")
    @CsvSource(value = {"WHITE:false", "EMPTY:false", "BLACK:true"}, delimiter = ':')
    void isBlack(Team team, boolean isBlack) {
        assertThat(team.isBlack()).isEqualTo(isBlack);
    }
}
