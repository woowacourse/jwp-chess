package chess;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import chess.dao.RoomDao;
import chess.dao.SquareDao;
import chess.domain.Status;
import chess.dto.BoardDto;
import chess.dto.MoveDto;
import chess.entity.Room;

public class ChessServiceTest {

    private ChessService chessService;
    private final RoomDao roomDao = new FakeRoomDao();
    private final SquareDao squareDao = new FakeSquareDao();
    private Long id;

    @BeforeEach
    void setUp() {
        chessService = new ChessService(roomDao, squareDao);
        Room room = chessService.createRoom("roma", "pw");
        id = room.getId();
    }

    @Test
    void createRoom() {
        Room actual = chessService.createRoom("sojukang", "pw");
        assertAll(
            () -> assertThat(actual.getName()).isEqualTo("sojukang"),
            () ->  assertThat(actual.getPassword()).isEqualTo("pw")
        );
    }

    @Test
    void startNewGame() {
        BoardDto boardDto = chessService.startNewGame(id);

        assertThat(boardDto.getTurn()).isEqualTo("white");
    }

    @Test
    void load() {
        chessService.startNewGame(id);

        BoardDto boardDto = chessService.load(id);

        assertAll(
            () -> assertThat(boardDto.getTurn()).isEqualTo("white"),
            () -> assertThat(boardDto.getBoard()).hasSize(64)
        );
    }

    @Test
    void move() {
        chessService.startNewGame(id);
        BoardDto actual = chessService.move(id, new MoveDto("a2", "a4"));

        assertAll(
            () -> assertThat(actual.getTurn()).isEqualTo("black"),
            () -> assertThat(actual.getBoard()).containsEntry("a2", "empty"),
            () -> assertThat(actual.getBoard()).containsEntry("a4", "white_pawn")
        );
    }

    @Test
    void status() {
        chessService.startNewGame(id);
        Status actual = chessService.status(id);

        assertAll(
            () -> assertThat(actual.getWhiteScore()).isEqualTo(38.0),
            () -> assertThat(actual.getBlackScore()).isEqualTo(38.0)
        );
    }

    @ParameterizedTest
    @CsvSource(value = {"pw:true", "pww:false"}, delimiter = ':')
    @DisplayName("delete로 id, password를 받아 Room, square 정보를 지운다.")
    void delete(String password, boolean expected) {
        assertThat(chessService.delete(id, password)).isEqualTo(expected);
    }
}
