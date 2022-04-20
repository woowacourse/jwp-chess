package chess.model.piece;

import chess.model.board.ConsoleBoard;
import chess.model.square.File;
import chess.model.square.Rank;
import chess.model.square.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static chess.model.piece.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class QueenTest {

    private ConsoleBoard consoleBoard;

    @BeforeEach
    public void setUp() {
        consoleBoard = new ConsoleBoard();
    }
    
    @Test
    void movable() {
        Queen queen = new Queen(Team.BLACK);

        assertAll(
                () -> assertThat(queen.movable(D4, B6)).isTrue(),
                () -> assertThat(queen.movable(D4, B2)).isTrue(),
                () -> assertThat(queen.movable(D4, F6)).isTrue(),
                () -> assertThat(queen.movable(D4, F2)).isTrue(),
                () -> assertThat(queen.movable(D4, D8)).isTrue(),
                () -> assertThat(queen.movable(D4, D1)).isTrue(),
                () -> assertThat(queen.movable(D4, A4)).isTrue(),
                () -> assertThat(queen.movable(D4, H4)).isTrue());
    }

    @Test
    void cannotMovable() {
        Queen queen = new Queen(Team.BLACK);

        assertAll(
                () -> assertThat(queen.movable(D4, E6)).isFalse(),
                () -> assertThat(queen.movable(D4, F5)).isFalse(),
                () -> assertThat(queen.movable(D4, F3)).isFalse(),
                () -> assertThat(queen.movable(D4, E2)).isFalse(),
                () -> assertThat(queen.movable(D4, C2)).isFalse(),
                () -> assertThat(queen.movable(D4, B3)).isFalse(),
                () -> assertThat(queen.movable(D4, B5)).isFalse(),
                () -> assertThat(queen.movable(D4, C6)).isFalse());
    }


    @Test
    void cannotMovableToSameColor() {
        Queen queen = new Queen(Team.BLACK);
        Square source = Square.of(File.D, Rank.EIGHT);
        Square target = Square.of(File.D, Rank.SEVEN);

        assertThat(queen.canMoveWithoutObstacle(consoleBoard, source, target)).isFalse();
    }

    @Test
    void cannotMovableAboveObstacle() {
        Queen queen = new Queen(Team.BLACK);
        Square source = Square.of(File.D, Rank.EIGHT);
        Square target = Square.of(File.D, Rank.SIX);

        assertThat(queen.canMoveWithoutObstacle(consoleBoard, source, target)).isFalse();
    }
}
