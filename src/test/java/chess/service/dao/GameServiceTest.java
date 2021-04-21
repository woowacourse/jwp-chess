package chess.service.dao;

import chess.controller.dto.BoardDto;
import chess.controller.dto.ScoresDto;
import chess.domain.ChessGame;
import chess.domain.board.position.Position;
import chess.domain.piece.Owner;
import chess.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class GameServiceTest {

    @Autowired GameService gameService;
    private Long roomId;

    @BeforeEach
    void beforeEach() {
        roomId = 123L;
        gameService.create(roomId);
    }

    @Test
    void testShow() {
        List<String> positions = gameService.show(roomId, new Position("b2"));

        assertThat(positions).isEqualTo(Arrays.asList("b3", "b4"));
    }

    @Test
    void testLoadChessGame() {
        Map<String, String> loadChessBoard = gameService.loadChessGame(roomId).boardDto();
        Map<String, String> newBoardDto = ChessGame.initNew().boardDto();

        assertThat(loadChessBoard).isEqualTo(newBoardDto);
    }

    @Test
    void testLoadNotValidChessGame() {
        assertThatIllegalStateException()
                .isThrownBy(() -> gameService.loadChessGame(1L))
                .withMessage("게임 정보를 찾을 수 없습니다.");
    }

    @Test
    void testisGameEnd() {
        assertThat(gameService.loadChessGame(roomId).isGameEnd()).isFalse();
    }

    @Test
    void testDelete() {
        gameService.delete(roomId);

        assertThatIllegalStateException()
                .isThrownBy(() -> gameService.loadChessGame(roomId))
                .withMessage("게임 정보를 찾을 수 없습니다.");
    }

    @Test
    void testMove() {
        gameService.move(roomId, new Position("a2"), new Position("a4"));

        gameService.move(roomId, new Position("a7"), new Position("a5"));

        gameService.move(roomId, new Position("a1"), new Position("a3"));

        Map<String, String> board = gameService.loadChessGame(roomId).boardDto();

        assertThat(board.get("a3")).isEqualTo("&#9814;");
    }

    @Test
    void testScoresDto() {
        ScoresDto score = gameService.scores(roomId);

        assertThat(score.getBlackScore()).isEqualTo(38);
        assertThat(score.getWhiteScore()).isEqualTo(38);
    }

    @Test
    void testBoardDto() {
        BoardDto board = gameService.board(roomId);

        assertThat(board.getBoard().get("a1")).isEqualTo("&#9814;");
    }

    @Test
    void testWinner() {
        gameService.move(roomId, new Position("d2"), new Position("d4"));
        gameService.move(roomId, new Position("e7"), new Position("e5"));
        gameService.move(roomId, new Position("d4"), new Position("e5"));
        gameService.move(roomId, new Position("d7"), new Position("d6"));
        gameService.move(roomId, new Position("e5"), new Position("d6"));
        gameService.move(roomId, new Position("e8"), new Position("e7"));
        gameService.move(roomId, new Position("d6"), new Position("e7"));

        assertThat(gameService.winner(roomId)).isEqualTo(Arrays.asList(Owner.WHITE));
    }
}
