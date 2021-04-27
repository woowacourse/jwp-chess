package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.game.ChessGame;
import chess.domain.piece.Color;
import chess.dto.MoveDto;
import chess.repository.ChessRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    private ChessRepository chessRepository;
    @InjectMocks
    private GameService gameService;


    @Test
    @DisplayName("게임 load 잘 되는지 확인")
    void loadGame() {
        ChessGame chessGame = new ChessGame(1L, Color.WHITE, false, new ChessBoard(), "test");
        given(chessRepository.loadGame(anyLong())).willReturn(chessGame);
        assertThat(gameService.loadGame(1L)).isEqualTo(chessGame);
    }

    @Test
    @DisplayName("기물 이동할 때 load 와 save 확인")
    void move() {
        given(chessRepository.loadGame(anyLong())).willReturn(
            new ChessGame(1L, Color.WHITE, false, new ChessBoard(), "test")
        );

        gameService.move(1L, new MoveDto("b2", "b4"));
        verify(chessRepository, times(1))
            .saveGame(1L, chessRepository.loadGame(1L));
    }

    @Test
    @DisplayName("킹이 죽었을 때 finish 실행")
    void checkGameOver() {
        ChessGame chessGame = new ChessGame(1L, Color.WHITE, false, new ChessBoard(), "test");
        chessGame.move(Position.of("b1"), Position.of("c3"));
        chessGame.move(Position.of("b7"), Position.of("b6"));
        chessGame.move(Position.of("c3"), Position.of("d5"));
        chessGame.move(Position.of("b6"), Position.of("b5"));
        chessGame.move(Position.of("d5"), Position.of("f6"));
        chessGame.move(Position.of("b5"), Position.of("b4"));
        chessGame.move(Position.of("f6"), Position.of("e8"));

        given(chessRepository.loadGame(anyLong())).willReturn(
            chessGame
        );
        gameService.move(1L, new MoveDto("a7", "a6"));
        verify(chessRepository, times(1))
            .finish(1L);
    }

    @Test
    @DisplayName("finish 실행 확인")
    void finish() {
        gameService.finish(1L);
        verify(chessRepository, times(1))
            .finish(1L);
    }

    @Test
    @DisplayName("재시작 시 새 게임 생성과 load 를 잘 하는지 확인")
    void restart() {
        gameService.restart(1L);
        verify(chessRepository, times(1)).restart(eq(1L), any());
        verify(chessRepository, times(1)).loadGame(1L);
    }
}
