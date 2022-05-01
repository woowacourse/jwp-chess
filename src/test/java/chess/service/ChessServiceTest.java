package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.dao.FakeRoomDao;
import chess.dao.FakeSquareDao;
import chess.dao.RoomDao;
import chess.dao.SquareDao;
import chess.domain.game.Status;
import chess.dto.BoardDto;
import chess.dto.MoveDto;
import chess.dto.PasswordDto;
import chess.dto.RoomCreationDto;
import chess.dto.RoomDto;
import chess.entity.RoomEntity;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChessServiceTest {

    private ChessService chessService;
    private final RoomDao roomDao = new FakeRoomDao();
    private final SquareDao squareDao = new FakeSquareDao();

    @BeforeEach
    void setUp() {
        chessService = new ChessService(roomDao, squareDao);
        chessService.createRoom(new RoomCreationDto("roma", ""));
    }

    @Test
    void createRoom() {
        long actual = chessService.createRoom(new RoomCreationDto("sojukang", ""));
        assertThat(actual).isEqualTo(2);
    }

    @Test
    void startNewGame() {
        BoardDto boardDto = chessService.startNewGame(1L);

        assertThat(boardDto.getTurn()).isEqualTo("white");
    }

    @Test
    void load() {
        chessService.startNewGame(1L);

        BoardDto boardDto = chessService.load(1L);

        assertAll(() -> assertThat(boardDto.getTurn()).isEqualTo("white"),
                () -> assertThat(boardDto.getBoard()).hasSize(64));
    }

    @Test
    void move() {
        chessService.startNewGame(1L);
        BoardDto actual = chessService.move(1L, new MoveDto("a2", "a4"));

        assertAll(() -> assertThat(actual.getTurn()).isEqualTo("black"),
                () -> assertThat(actual.getBoard()).containsEntry("a2", "empty"),
                () -> assertThat(actual.getBoard()).containsEntry("a4", "white_pawn"));
    }

    @Test
    void status() {
        chessService.startNewGame(1L);
        Status actual = chessService.status(1L);

        assertAll(() -> assertThat(actual.getWhiteScore()).isEqualTo(38.0),
                () -> assertThat(actual.getBlackScore()).isEqualTo(38.0));
    }

    @Test
    void list() {
        chessService.createRoom(new RoomCreationDto("sojukang", ""));

        List<RoomDto> list = chessService.list();
        assertThat(list).hasSize(2);
    }

    @Test
    void end() {
        chessService.startNewGame(1L);
        chessService.end(1L);
        assertThatThrownBy(() -> chessService.load(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 ID에 체스게임이 초기화되지 않았습니다.");
    }

    @Test
    void delete() {
        chessService.delete(1L, new PasswordDto(""));
        Optional<RoomEntity> roomOptional = roomDao.findById(1L);
        assertThat(roomOptional).isEmpty();
    }
}
