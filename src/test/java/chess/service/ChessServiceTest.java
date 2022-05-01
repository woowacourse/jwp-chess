package chess.service;

import chess.dao.ChessDao;
import chess.dao.ChessDaoImpl;
import chess.domain.piece.Color;
import chess.domain.state.Result;
import chess.dto.GameRoomDto;
import chess.dto.MoveDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ChessServiceTest {
    //TODO h2 mysql 비교
    private int gameId;

    private ChessDao chessDao;
    private ChessService chessService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessDao = new ChessDaoImpl(jdbcTemplate);
        gameId = chessDao.initGame("칙촉조시제이", "123");
        chessService = new ChessService(chessDao);
    }

    @Test
    void newGame() {
        var chessGameDto = chessService.newGame(gameId);
        assertAll(() -> {
            assertThat(chessGameDto.getPositionsAndPieces().keySet().size()).isEqualTo(32);
            assertThat(chessGameDto.getWhiteScore().get(Color.WHITE)).isEqualTo(38);
            assertThat(chessGameDto.getBlackScore().get(Color.BLACK)).isEqualTo(38);
            assertThat(chessGameDto.getResult()).isEqualTo(Result.EMPTY);
        });
    }

    @Test
    void move() {
        chessService.newGame(gameId);
        var chessGameDto = chessService.move(new MoveDto(gameId, "A2", "A3"));
        assertAll(() -> {
            assertThat(chessGameDto.getPositionsAndPieces().containsKey("A2")).isFalse();
            assertThat(chessGameDto.getPositionsAndPieces().containsKey("A3")).isTrue();
        });
    }

    @Test
    @DisplayName("이전 게임정보 가져오기")
    void loadGame() {
        chessService.newGame(gameId);
        chessService.move(new MoveDto(gameId, "A2", "A3"));
        var chessGameDto = chessService.loadGame(gameId);
        assertAll(() -> {
            assertThat(chessGameDto.getPositionsAndPieces().containsKey("A2")).isFalse();
            assertThat(chessGameDto.getPositionsAndPieces().containsKey("A3")).isTrue();
            assertThat(chessGameDto.getWhiteScore().get(Color.WHITE)).isEqualTo(38);
            assertThat(chessGameDto.getBlackScore().get(Color.BLACK)).isEqualTo(38);
            assertThat(chessGameDto.getResult()).isEqualTo(Result.EMPTY);
        });
    }

    @Test
    @DisplayName("이전 게임정보가 없을 경우 예외발생")
    void noData() {
        assertThatThrownBy(() -> chessService.loadGame(gameId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("게임을 시작해 주세요.");
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않는경우")
    void wrongPassword() {
        var wrongPassword = "12";
        assertThatThrownBy(() ->
                chessService.deleteGame(new GameRoomDto("칙촉조시제이", wrongPassword, gameId))
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("게임이 진행중일 경우 삭제요청시 예외발생")
    void runningGame() {
        chessService.newGame(gameId);
        assertThatThrownBy(() ->
                chessService.deleteGame(new GameRoomDto("칙촉조시제이", "123", gameId))
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게임이 종료되지 않아 삭제할 수 없습니다.");
    }

    @Test
    void deleteGame() {
        chessService.newGame(gameId);
        whiteWinCommand();
        chessService.deleteGame(new GameRoomDto("칙촉조시제이", "123", gameId));
        assertThatThrownBy(() -> chessService.loadGame(gameId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    private void whiteWinCommand() {
        List<MoveDto> moveDtos = List.of(
                new MoveDto(gameId, "d2", "d4"),
                new MoveDto(gameId, "e7", "e5"),
                new MoveDto(gameId, "d4", "d5"),
                new MoveDto(gameId, "e8", "e7"),
                new MoveDto(gameId, "d5", "d6"),
                new MoveDto(gameId, "e5", "e4"),
                new MoveDto(gameId, "d6", "e7")
        );
        for (MoveDto moveDto : moveDtos) {
            chessService.move(moveDto);
        }
    }
}
