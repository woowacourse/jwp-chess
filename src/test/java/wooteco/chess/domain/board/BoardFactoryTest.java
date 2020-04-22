package wooteco.chess.domain.board;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.chess.domain.piece.GamePiece;
import wooteco.chess.domain.player.User;

public class BoardFactoryTest {

    @Test
    @DisplayName("보드 생성")
    void create() {
        Board board = BoardFactory.createInitialBoard();
        String map = board.getBoard()
                .values()
                .stream()
                .map(GamePiece::getName)
                .collect(Collectors.joining(""));
        assertThat(map).isEqualTo("RNBQKBNRPPPPPPPP................................pppppppprnbqkbnr");
    }
}
