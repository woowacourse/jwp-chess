package springchess.model.piece;

import chess.model.board.ConsoleBoard;
import chess.model.piece.Knight;
import chess.model.piece.Team;
import chess.model.square.File;
import chess.model.square.Rank;
import chess.model.square.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static chess.model.piece.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class KnightTest {

    private ConsoleBoard consoleBoard;

    @BeforeEach
    public void setUp() {
        consoleBoard = new ConsoleBoard();
    }
    
    @Test
    void createKnight() {
        chess.model.piece.Knight knight = new chess.model.piece.Knight(chess.model.piece.Team.BLACK);

        assertThat(knight).isInstanceOf(chess.model.piece.Knight.class);
    }

    @Test
    void movable() {
        chess.model.piece.Knight knight = new chess.model.piece.Knight(chess.model.piece.Team.BLACK);

        assertAll(
                () -> assertThat(knight.movable(D4, E6)).isTrue(),
                () -> assertThat(knight.movable(D4, F5)).isTrue(),
                () -> assertThat(knight.movable(D4, F3)).isTrue(),
                () -> assertThat(knight.movable(D4, E2)).isTrue(),
                () -> assertThat(knight.movable(D4, C2)).isTrue(),
                () -> assertThat(knight.movable(D4, B3)).isTrue(),
                () -> assertThat(knight.movable(D4, B5)).isTrue(),
                () -> assertThat(knight.movable(D4, C6)).isTrue(),
                () -> assertThat(knight.movable(C2, D4)).isTrue());

    }

    @Test
    void cannotMovable() {
        chess.model.piece.Knight knight = new chess.model.piece.Knight(chess.model.piece.Team.BLACK);

        assertAll(
                () -> assertThat(knight.movable(D4, D5)).isFalse(),
                () -> assertThat(knight.movable(D4, F2)).isFalse(),
                () -> assertThat(knight.movable(D4, B2)).isFalse(),
                () -> assertThat(knight.movable(D4, F6)).isFalse());
    }

    @Test
    void cannotMovableToSameColor() {
        chess.model.piece.Knight knight = new Knight(Team.BLACK);
        Square source = Square.of(File.B, Rank.EIGHT);
        Square target = Square.of(File.D, Rank.SEVEN);

        assertThat(knight.canMoveWithoutObstacle(consoleBoard, source, target)).isFalse();
    }
}
