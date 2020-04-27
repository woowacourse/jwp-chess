package wooteco.chess.domain.judge;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Team;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class JudgeTest {

    @Test
    @DisplayName("팀별 점수 계산 테스트")
    void getScoreByTeam() {
        Judge judge = new Judge();
        Board board = BoardFactory.create();
        board.move(Position.of(2, 5), Position.of(4, 5));
        board.move(Position.of(7, 6), Position.of(5, 6));
        board.move(Position.of(1, 4), Position.of(5, 8));
        board.move(Position.of(8, 5), Position.of(7, 6));
        board.move(Position.of(5, 8), Position.of(7, 6));

        assertThat(judge.getScoreByTeam(board, Team.BLACK)).isEqualTo(38);
    }

    @Test
    @DisplayName("승자 확인 테스트 - White 승리")
    void findWinnerWhiteWin() {
        Judge judge = new Judge();
        Board board = BoardFactory.create();
        board.move(Position.of(2, 5), Position.of(4, 5));
        board.move(Position.of(7, 6), Position.of(5, 6));
        board.move(Position.of(1, 4), Position.of(5, 8));
        board.move(Position.of(8, 5), Position.of(7, 6));
        board.move(Position.of(5, 8), Position.of(7, 6));

        assertThat(judge.findWinner(board)).isEqualTo(Optional.of(Team.WHITE));
    }

    @Test
    @DisplayName("승자 확인 테스트 - Black 승리")
    void findWinnerBlackWin() {
        Judge judge = new Judge();
        Board board = BoardFactory.create();
        board.move(Position.of(2, 6), Position.of(4, 6));
        board.move(Position.of(7, 5), Position.of(6, 5));
        board.move(Position.of(1, 5), Position.of(2, 6));
        board.move(Position.of(8, 4), Position.of(4, 8));
        board.move(Position.of(2, 6), Position.of(3, 7));
        board.move(Position.of(4, 8), Position.of(3, 7));

        assertThat(judge.findWinner(board)).isEqualTo(Optional.of(Team.BLACK));
    }

    @Test
    @DisplayName("승자 확인 테스트 - 무승부")
    void findWinnerTieGame() {
        Judge judge = new Judge();
        Board board = BoardFactory.create();
        board.move(Position.of(2, 5), Position.of(4, 5));
        board.move(Position.of(7, 6), Position.of(5, 6));
        board.move(Position.of(1, 4), Position.of(5, 8));
        board.move(Position.of(8, 5), Position.of(7, 6));

        assertThat(judge.findWinner(board)).isEqualTo(Optional.empty());
    }
}
