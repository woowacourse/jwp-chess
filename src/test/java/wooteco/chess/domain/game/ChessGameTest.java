package wooteco.chess.domain.game;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import wooteco.chess.domain.MoveParameter;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.initializer.AutomatedBoardInitializer;
import wooteco.chess.domain.board.initializer.EnumRepositoryBoardInitializer;
import wooteco.chess.domain.piece.PieceState;
import wooteco.chess.domain.player.Team;
import wooteco.chess.domain.position.Position;

class ChessGameTest {

    private ChessGame chessGame;

    @BeforeEach
    void setUp() {
        String title = "방 제목";
        Board board = Board.of(new AutomatedBoardInitializer());
        Turn turn = Turn.from(Team.WHITE);
        chessGame = ChessGame.of(title, board, turn);
    }

    @Test
    void create() {
        Long id = 1L;
        String title = "방 제목";
        Board board = Board.of(new AutomatedBoardInitializer());
        Turn turn = Turn.from(Team.WHITE);
        ChessGame chessGame = ChessGame.of(id, title, board, turn);
        assertThat(chessGame.getId()).isEqualTo(1L);
        assertThat(chessGame).isInstanceOf(ChessGame.class);
    }

    @Test
    void isEnd() {
        assertThat(chessGame.isEnd()).isFalse();
    }

    @Test
    void move() {
        MoveParameter moveParameter = MoveParameter.of(Position.of("A2"), Position.of("A3"));
        chessGame.move(moveParameter);
        assertThat(chessGame.getTurn()).isEqualTo(Team.BLACK);
    }

    @Test
    void moveException() {
        Map<Position, PieceState> boardMap = new EnumRepositoryBoardInitializer().create();
        boardMap.remove(Position.of("E1"));
        Board board = Board.of(boardMap);
        ChessGame chessGame = ChessGame.of("hi", board, Turn.from(Team.BLACK));
        MoveParameter moveParameter = MoveParameter.of(Position.of("A2"), Position.of("A3"));
        assertThatThrownBy(() -> chessGame.move(moveParameter))
            .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void getStatus() {
        Map<Team, Double> status = chessGame.getStatus();
        assertThat(status.get(Team.WHITE)).isEqualTo(38);
        assertThat(status.get(Team.BLACK)).isEqualTo(38);
    }

    @Test
    void getScore() {
        assertThat(chessGame.getScore()).isEqualTo(38);
    }

    @Test
    void getWinner() {
        Map<Position, PieceState> boardMap = new EnumRepositoryBoardInitializer().create();
        boardMap.remove(Position.of("E1"));
        Board board = Board.of(boardMap);
        ChessGame chessGame = ChessGame.of("hi", board, Turn.from(Team.BLACK));
        assertThat(chessGame.getWinner()).isEqualTo(Team.BLACK);
    }

    @Test
    void getWinnerException() {
        assertThatThrownBy(() -> chessGame.getWinner())
            .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void getMovablePositions() {
        List<Position> positions = chessGame.getMovablePositions(Position.of("A2"));
        assertThat(positions).contains(Position.of("A3"), Position.of("A4"));
    }

    @Test
    void get() {
        assertThat(chessGame.getTitle()).isEqualTo("방 제목");
    }
}