package wooteco.chess.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.board.Position;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelParserTest {

    @Test
    @DisplayName("빈 보드 파싱 테스트")
    void parseBlankBoard() {
        Map<String, Object> board = ModelParser.parseBlankBoard();
        assertThat(board.size()).isEqualTo(64);
    }

    @Test
    @DisplayName("보드 파싱 테스트")
    void parseBoard() {
        Board board = BoardFactory.create();
        assertThat(ModelParser.parseBoard(board)).isInstanceOf(Map.class);
    }

    @Test
    @DisplayName("보드 파싱 테스트 - MovablePlaces 포함")
    void parseBoardWithMovablePlaces() {
        Board board = BoardFactory.create();
        List<Position> movablePlaces = board.findMovablePositions(Position.of(7, 1));
        assertThat(ModelParser.parseBoard(board, movablePlaces)).isInstanceOf(Map.class);
    }

    @Test
    @DisplayName("MovablePlaces 파싱 테스트")
    void parseMovablePlaces() {
        Board board = BoardFactory.create();
        List<Position> movablePlaces = board.findMovablePositions(Position.of(7, 1));
        assertThat(ModelParser.parseMovablePlaces(movablePlaces)).isInstanceOf(Map.class);
    }
}
