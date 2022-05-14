package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import chess.dao.ChessDao;
import chess.domain.board.Board;
import chess.domain.board.BoardInitializer;
import chess.domain.piece.Color;
import chess.domain.state.Result;
import chess.dto.ChessGameDto;
import chess.dto.GameRoomDto;
import chess.dto.MoveDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class ChessServiceTest {
    private int gameId;
    private ChessDao chessDao;
    private ChessService chessService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessDao = new ChessDao(jdbcTemplate);
        gameId = chessDao.initGame("칙촉조시제이", "123");
        chessDao.saveGame(gameId, new Board(new BoardInitializer()));
        chessService = new ChessService(chessDao);
    }

    @Test
    void newGame() {
        var chessGameDto = chessService.resetGame(gameId);
        assertAll(() -> {
            assertThat(chessGameDto.getPositionsAndPieces().keySet().size()).isEqualTo(32);
            assertThat(chessGameDto.getWhiteScore().get(Color.WHITE)).isEqualTo(38);
            assertThat(chessGameDto.getBlackScore().get(Color.BLACK)).isEqualTo(38);
            assertThat(chessGameDto.getResult()).isEqualTo(Result.EMPTY);
        });
    }

    @Test
    void move() {
        var chessGameDto = chessService.move(new MoveDto(gameId, "A2", "A3"));
        assertAll(() -> {
            assertThat(chessGameDto.getPositionsAndPieces().containsKey("A2")).isFalse();
            assertThat(chessGameDto.getPositionsAndPieces().containsKey("A3")).isTrue();
        });
    }

    @Test
    @DisplayName("이전 게임정보 가져오기")
    void findGame() {
        chessService.move(new MoveDto(gameId, "A2", "A3"));
        var chessGameDto = ChessGameDto.from(chessService.findChessGameById(gameId));
        assertAll(() -> {
            assertThat(chessGameDto.getPositionsAndPieces().containsKey("A2")).isFalse();
            assertThat(chessGameDto.getPositionsAndPieces().containsKey("A3")).isTrue();
            assertThat(chessGameDto.getWhiteScore().get(Color.WHITE)).isEqualTo(38);
            assertThat(chessGameDto.getBlackScore().get(Color.BLACK)).isEqualTo(38);
            assertThat(chessGameDto.getResult()).isEqualTo(Result.EMPTY);
        });
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
        chessService.resetGame(gameId);
        assertThatThrownBy(() ->
                chessService.deleteGame(new GameRoomDto("칙촉조시제이", "123", gameId))
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게임이 종료되지 않았습니다.");
    }

    @Test
    void deleteGame() {
        whiteWinCommand();

        assertDoesNotThrow(() -> chessService.deleteGame(new GameRoomDto("칙촉조시제이", "123", gameId)));
    }

    private void whiteWinCommand() {
        List<MoveDto> moveDtos = List.of(
                new MoveDto(gameId, "D2", "D4"),
                new MoveDto(gameId, "E7", "E5"),
                new MoveDto(gameId, "D4", "D5"),
                new MoveDto(gameId, "E8", "E7"),
                new MoveDto(gameId, "D5", "D6"),
                new MoveDto(gameId, "E5", "E4"),
                new MoveDto(gameId, "D6", "E7")
        );
        for (MoveDto moveDto : moveDtos) {
            chessService.move(moveDto);
        }
    }
}
