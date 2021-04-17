package chess.utils.position.converter;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.board.Board;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PositionConverterTest {

    @DisplayName("위치 -> 셀 상태 리스트의 인덱스 변환 테스트")
    @Test
    void convert() {
        Board board = new Board(""
            + ".KR....."
            + "P.PB...."
            + ".P..Q..."
            + "........"
            + ".....nq."
            + ".....p.p"
            + ".....pp."
            + "....r..."
        );

        String boardStatus = board.getBoardStatus();

        int cellIndexOfBlackPawn = PositionConverter.convertToCellsStatusIndex("a7");
        int cellIndexOfBlackKing = PositionConverter.convertToCellsStatusIndex("b8");
        int cellIndexOfEmpty1 = PositionConverter.convertToCellsStatusIndex("b7");
        int cellIndexOfWhiteKnight = PositionConverter.convertToCellsStatusIndex("f4");
        int cellIndexOfWhiteQueen = PositionConverter.convertToCellsStatusIndex("g4");
        int cellIndexOfEmpty2 = PositionConverter.convertToCellsStatusIndex("g3");

        assertThat(String.valueOf(boardStatus.charAt(cellIndexOfBlackPawn))).isEqualTo("P");
        assertThat(String.valueOf(boardStatus.charAt(cellIndexOfBlackKing))).isEqualTo("K");
        assertThat(String.valueOf(boardStatus.charAt(cellIndexOfEmpty1))).isEqualTo(".");
        assertThat(String.valueOf(boardStatus.charAt(cellIndexOfWhiteKnight))).isEqualTo("n");
        assertThat(String.valueOf(boardStatus.charAt(cellIndexOfWhiteQueen))).isEqualTo("q");
        assertThat(String.valueOf(boardStatus.charAt(cellIndexOfEmpty2))).isEqualTo(".");
    }
}
