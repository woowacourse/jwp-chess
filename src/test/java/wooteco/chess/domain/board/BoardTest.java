package wooteco.chess.domain.board;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.chess.domain.board.initializer.AutomatedBoardInitializer;
import wooteco.chess.domain.board.initializer.EnumRepositoryBoardInitializer;
import wooteco.chess.domain.game.Turn;
import wooteco.chess.domain.piece.PieceState;
import wooteco.chess.domain.piece.implementation.piece.Knight;
import wooteco.chess.domain.player.Team;
import wooteco.chess.domain.position.Position;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = Board.of(new EnumRepositoryBoardInitializer());
    }

    @Test
    @DisplayName("board는 Map<Position, PieceState>를 받아서 생성")
    void initialize() {
        Map<Position, PieceState> boardMap = new AutomatedBoardInitializer().create();
        Board board = Board.of(boardMap);
    }

    @Test
    void move() {
        board.move(Position.of("B8"), Position.of("A6"), Turn.from(Team.BLACK));
        Map<Position, PieceState> piecePosition = board.getBoard();

        assertThat(piecePosition.get(Position.of("A6")))
            .isInstanceOf(Knight.class);
        assertThat(piecePosition.get(Position.of("B8")))
            .isNull();
    }

    @Test
    void move_invalidSource_exception() {
        assertThatThrownBy(() -> board.move(Position.of("A6"), Position.of("A5"), Turn.from(Team.BLACK)))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> board.move(Position.of("A7"), Position.of("A6"), Turn.from(Team.WHITE)))
            .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    void getMovablePositions() {
        List<Position> positions = board.getMovablePositions(Position.of("A2"), Turn.from(Team.WHITE));
        assertThat(positions).contains(Position.of("A3"), Position.of("A4"));
    }

    @Test
    void isEnd() {
        assertThat(this.board.isEnd()).isFalse();
        Map<Position, PieceState> board = new AutomatedBoardInitializer().create();
        board.remove(Position.of("E1"));
        this.board = Board.of(board);
        assertThat(this.board.isEnd()).isTrue();
    }

    @Test
    void status() {
        assertThat(this.board.getScores(Team.WHITE)).isEqualTo(38);
        assertThat(this.board.getScores(Team.BLACK)).isEqualTo(38);
        Map<Position, PieceState> board = new AutomatedBoardInitializer().create();
        board.remove(Position.of("A1"));
        this.board = Board.of(board);
        assertThat(this.board.getScores(Team.WHITE)).isEqualTo(33);
    }
}