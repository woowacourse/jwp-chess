package chess.player;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spring.chess.player.ChessSet;
import spring.chess.player.Player;
import spring.chess.team.Team;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerTest {

    @Test
    @DisplayName("팀이 같은지 : 참")
    void isSame1() {
        Player player = new Player(new ChessSet(Collections.EMPTY_MAP), Team.WHITE);

        assertThat(player.is(Team.WHITE)).isTrue();
    }

    @Test
    @DisplayName("팀이 같은지 : 거짓")
    void isSame2() {
        Player player = new Player(new ChessSet(Collections.EMPTY_MAP), Team.WHITE);

        assertThat(player.is(Team.BLACK)).isFalse();
    }

    @Test
    @DisplayName("팀이 같지 않은지 : 참")
    void isNotSame() {
        Player player = new Player(new ChessSet(Collections.EMPTY_MAP), Team.WHITE);

        assertThat(player.isNotSame(Team.BLACK)).isTrue();
    }

    @Test
    @DisplayName("팀이 같지 않은지 : 거짓")
    void isNotSame2() {
        Player player = new Player(new ChessSet(Collections.EMPTY_MAP), Team.WHITE);

        assertThat(player.isNotSame(Team.WHITE)).isFalse();
    }
}