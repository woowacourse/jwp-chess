package chess.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import chess.domain.piece.detail.Team;

class ResultTest {

    @ParameterizedTest
    @CsvSource({"38,36,BLACK", "35,38,WHITE", "30,30,NONE"})
    void byScore(final Double blackScore, final Double whiteScore, final String rawTeam) {
        final Team winner = Team.valueOf(rawTeam);
        final Result result = Result.byScore(blackScore, whiteScore);
        assertAll(
                () -> assertThat(result.getWinner()).isSameAs(winner),
                () -> assertThat(result.getBlackScore()).isEqualTo(blackScore),
                () -> assertThat(result.getWhiteScore()).isEqualTo(whiteScore)
        );
    }
}
