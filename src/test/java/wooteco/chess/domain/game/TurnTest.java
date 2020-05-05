package wooteco.chess.domain.game;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import wooteco.chess.domain.player.Team;

class TurnTest {

    private Turn turn;

    @Test
    void createWhite() {
        turn = Turn.from(Team.WHITE);
        assertThat(turn.isSameTeam(Team.WHITE));
    }

    @Test
    void createBlack() {
        turn = Turn.from(Team.BLACK);
        assertThat(turn.isSameTeam(Team.BLACK));
    }

    @Test
    void switchTurn() {
        turn = Turn.from(Team.WHITE);
        turn.switchTurn();
        assertThat(turn.getTeam()).isEqualTo(Team.BLACK);
        turn.switchTurn();
        assertThat(turn.getTeam()).isEqualTo(Team.WHITE);
    }
}