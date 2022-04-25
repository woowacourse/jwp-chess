package chess;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.dao.RoomDao;
import chess.dao.SquareDao;
import chess.domain.Status;
import chess.dto.BoardDto;
import chess.dto.MoveDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChessServiceTest {

    private ChessService chessService;
    private final RoomDao roomDao = new FakeRoomDao();
    private final SquareDao squareDao = new FakeSquareDao();

    @BeforeEach
    void setUp() {
        chessService = new ChessService(roomDao, squareDao);
        chessService.createRoom("roma");
    }

    @Test
    void createRoom() {
        boolean actual = chessService.createRoom("sojukang");
        assertThat(actual).isTrue();
    }

    @Test
    void startNewGame() {
        BoardDto boardDto = chessService.startNewGame("roma");

        assertThat(boardDto.getTurn()).isEqualTo("white");
    }

    @Test
    void load() {
        chessService.startNewGame("roma");

        BoardDto boardDto = chessService.load("roma");

        assertAll(
                () -> assertThat(boardDto.getTurn()).isEqualTo("white"),
                () -> assertThat(boardDto.getBoard()).hasSize(64)
        );
    }

    @Test
    void move() {
        chessService.startNewGame("roma");
        BoardDto actual = chessService.move("roma", new MoveDto("a2", "a4"));

        assertAll(
                () -> assertThat(actual.getTurn()).isEqualTo("black"),
                () -> assertThat(actual.getBoard()).containsEntry("a2", "empty"),
                () -> assertThat(actual.getBoard()).containsEntry("a4", "white_pawn")
        );
    }

    @Test
    void status() {
        chessService.startNewGame("roma");
        Status actual = chessService.status("roma");

        assertAll(
                () -> assertThat(actual.getWhiteScore()).isEqualTo(38.0),
                () -> assertThat(actual.getBlackScore()).isEqualTo(38.0)
        );
    }
}
