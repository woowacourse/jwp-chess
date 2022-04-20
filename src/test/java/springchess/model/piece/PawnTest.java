package springchess.model.piece;

import chess.model.board.ConsoleBoard;
import chess.model.piece.Pawn;
import chess.model.piece.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static chess.model.piece.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PawnTest {

    private ConsoleBoard consoleBoard;

    @BeforeEach
    public void setUp() {
        consoleBoard = new ConsoleBoard();
    }

    @Test
    void createPawn() {
        chess.model.piece.Pawn pawn = new chess.model.piece.Pawn(chess.model.piece.Team.BLACK);

        assertThat(pawn).isInstanceOf(chess.model.piece.Pawn.class);
    }


    @Test
    void firstSquareMovable() {
        chess.model.piece.Pawn pawn = new chess.model.piece.Pawn(chess.model.piece.Team.WHITE);

        assertAll(
                () -> assertThat(pawn.movable(consoleBoard, E2, E4)).isTrue(),
                () -> assertThat(pawn.movable(consoleBoard, C2, C3)).isTrue());
    }

    @Test
    void firstSquareCannotMovable() {
        chess.model.piece.Pawn pawn = new chess.model.piece.Pawn(chess.model.piece.Team.WHITE);

        assertAll(
                () -> assertThat(pawn.movable(consoleBoard, E2, E5)).isFalse(),
                () -> assertThat(pawn.movable(consoleBoard, E2, D3)).isFalse());
    }

    @Test
    void WhitePawnCannotMoveInLastColumn() {
        chess.model.piece.Pawn pawn = new Pawn(Team.WHITE);

        assertThat(pawn.movable(consoleBoard, E2, E1)).isFalse();
    }
}
