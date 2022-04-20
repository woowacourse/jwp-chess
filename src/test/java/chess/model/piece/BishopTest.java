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

public class BishopTest {

    private ConsoleBoard consoleBoard;

    @BeforeEach
    public void setUp() {
        consoleBoard = new ConsoleBoard();
    }

    @Test
    void createBishop() {
        Bishop bishop = new Bishop(Team.BLACK);

        assertThat(bishop).isInstanceOf(Bishop.class);
    }

    @Test
    void movable() {
        Bishop bishop = new Bishop(Team.BLACK);

        assertAll(
                () -> assertThat(bishop.movable(D4, B6)).isTrue(),
                () -> assertThat(bishop.movable(D4, B2)).isTrue(),
                () -> assertThat(bishop.movable(D4, F6)).isTrue(),
                () -> assertThat(bishop.movable(D4, F2)).isTrue());
    }

    @Test
    void cannotMovable() {
        Bishop bishop = new Bishop(Team.BLACK);

        assertThat(bishop.movable(D4, D5)).isFalse();
    }

    @Test
    void cannotMovableToSameColor() {
        Bishop bishop = new Bishop(Team.BLACK);
        Square source = Square.of(File.B, Rank.EIGHT);
        Square target = Square.of(File.A, Rank.SEVEN);

        assertThat(bishop.canMoveWithoutObstacle(consoleBoard, source, target)).isFalse();
    }

    @Test
    void cannotMovableAboveObstacle() {
        Bishop bishop = new Bishop(Team.BLACK);
        Square source = Square.of(File.B, Rank.EIGHT);
        Square target = Square.of(File.D, Rank.SIX);

        assertThat(bishop.canMoveWithoutObstacle(consoleBoard, source, target)).isFalse();
    }
}
