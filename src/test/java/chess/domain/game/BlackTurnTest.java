package chess.domain.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.board.Board;
import chess.domain.board.piece.Color;
import chess.domain.event.Event;
import chess.domain.event.MoveEvent;
import chess.domain.board.BoardFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class BlackTurnTest {

    private static final Event VALID_BLACK_MOVE = new MoveEvent("a7 a6");
    private static final Event VALID_WHITE_MOVE = new MoveEvent("a2 a3");

    private Game game;

    @BeforeEach
    void setUp() {
        Board board = BoardFactory.init();
        game = new BlackTurn(board);
    }

    @DisplayName("play 메서드는 인자로 받은 이벤트를 활용하여 다른 상태의 게임을 반환")
    @Nested
    class PlayTest {

        @Test
        void 흑색_체스말_이동_후_백색_턴_반환() {
            Game actual = game.play(VALID_BLACK_MOVE);

            assertThat(actual).isInstanceOf(WhiteTurn.class);
        }

        @Test
        void 흑색_턴에서_백색_체스말_이동시_예외발생() {
            assertThatThrownBy(() -> game.play(VALID_WHITE_MOVE))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("BLACK 진영이 움직일 차례입니다!");
        }
    }

    @Test
    void isValidTurn_메서드는_흑색을_받았을때_참() {
        boolean actual = game.isValidTurn(Color.BLACK);

        assertThat(actual).isTrue();
    }

    @Test
    void 보드_정보가_동일한_흑색_턴_인스턴스는_동일() {
        Board board = BoardFactory.init();
        Game game1 = new BlackTurn(board);
        Game game2 = new BlackTurn(board);

        assertThat(game1).isEqualTo(game2);
    }
}