package springchess.model.piece;

import chess.model.board.ConsoleBoard;
import chess.model.piece.Rook;
import chess.model.piece.Team;
import chess.model.square.File;
import chess.model.square.Rank;
import chess.model.square.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static chess.model.piece.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class RookTest {

    private ConsoleBoard consoleBoard;

    @BeforeEach
    public void setUp() {
        consoleBoard = new ConsoleBoard();
    }
    
    @Test
    void createRook() {
        chess.model.piece.Rook rook = new chess.model.piece.Rook(chess.model.piece.Team.BLACK);

        assertThat(rook).isInstanceOf(chess.model.piece.Rook.class);
    }

    @Test
    void movable() {
        chess.model.piece.Rook rook = new chess.model.piece.Rook(chess.model.piece.Team.BLACK);

        assertAll(
                () -> assertThat(rook.movable(D4, H4)).isTrue(),
                () -> assertThat(rook.movable(D4, A4)).isTrue(),
                () -> assertThat(rook.movable(D4, D1)).isTrue(),
                () -> assertThat(rook.movable(D4, D8)).isTrue());
    }

    @Test
    void cannotMovable() {
        chess.model.piece.Rook rook = new chess.model.piece.Rook(chess.model.piece.Team.BLACK);

        assertAll(
                () -> assertThat(rook.movable(D4, B6)).isFalse(),
                () -> assertThat(rook.movable(D4, B2)).isFalse(),
                () -> assertThat(rook.movable(D4, F6)).isFalse(),
                () -> assertThat(rook.movable(D4, F2)).isFalse());
    }

    @Test
    void cannotMovableToSameColor() {
        chess.model.piece.Rook rook = new chess.model.piece.Rook(chess.model.piece.Team.BLACK);
        Square source = Square.of(File.A, Rank.EIGHT);
        Square target = Square.of(File.A, Rank.SEVEN);

        assertThat(rook.canMoveWithoutObstacle(consoleBoard, source, target)).isFalse();
    }

    @Test
    void cannotMovableAboveObstacle() {
        chess.model.piece.Rook rook = new Rook(Team.BLACK);
        Square source = Square.of(File.A, Rank.EIGHT);
        Square target = Square.of(File.A, Rank.SIX);

        assertThat(rook.canMoveWithoutObstacle(consoleBoard, source, target)).isFalse();
    }
}
