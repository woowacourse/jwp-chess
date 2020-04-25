package wooteco.chess.domain.piece;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.chess.domain.ChessRunner;

import static org.assertj.core.api.Assertions.assertThat;

class TeamTest {
    @DisplayName("팀 순서 변경 테스트")
    @Test
    void changeTeam() {
        ChessRunner chessRunner = new ChessRunner();
        Team nextTeam = chessRunner.getCurrentTeam().changeTeam();
        assertThat(nextTeam.isSameTeamWith(Team.BLACK)).isTrue();
    }
}