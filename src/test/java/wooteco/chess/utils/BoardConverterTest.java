package wooteco.chess.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.strategy.NormalInitStrategy;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardConverterTest {
    @Test
    @DisplayName("보드를 스트링으로 바꾸는 기능 테스트")
    void convertBoardToString() {
        NormalInitStrategy strategy = new NormalInitStrategy();
        Board board = new Board(strategy.init());
        String result = "rnbqkbnrpppppppp................................PPPPPPPPRNBQKBNR";
        assertThat(BoardConverter.convertToString(board)).isEqualTo(result);
    }

    @DisplayName("스트링을 보드로 바꾸는 기능 테스트")
    @Test
    void convertStringToBoard() {
        NormalInitStrategy strategy = new NormalInitStrategy();
        Board board = new Board(strategy.init());
        assertThat(BoardConverter.convertToBoard("rnbqkbnrpppppppp................................PPPPPPPPRNBQKBNR", true))
                .isEqualTo(board);
    }
}
