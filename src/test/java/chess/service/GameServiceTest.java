package chess.service;

import chess.domain.board.ChessBoard;
import chess.domain.game.ChessGame;
import chess.domain.piece.Color;
import chess.repository.ChessRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private ChessRepository chessRepository;
    @InjectMocks
    private GameService gameService;

    @Test
    @DisplayName("move에 대한 테스트")
    void move() {
        ChessGame chessGame = new ChessGame(1L, Color.WHITE, false, new ChessBoard(), "test");
        given(chessRepository.loadGame(1L)).willReturn(chessGame);
        willDoNothing().given(chessRepository).saveGame(1L, chessGame);

        gameService.move(1L, "b2", "b3");

        then(chessRepository).should(times(1)).loadGame(1L);
        then(chessRepository).should(times(1)).saveGame(1L, chessGame);
        then(chessRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("게임 load에 대한 테스트")
    void loadGame() {
        ChessGame chessGame = new ChessGame(1L, Color.WHITE, false, new ChessBoard(), "test");
        given(chessRepository.loadGame(1L)).willReturn(chessGame);

        assertThat(gameService.loadGame(1L)).isEqualTo(chessGame);

        then(chessRepository).should(times(1)).loadGame(1L);
        then(chessRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("게임 종료에 대한 테스트")
    void terminateGame() {
        willDoNothing().given(chessRepository).terminateGame(1L);

        gameService.terminateGame(1L);

        then(chessRepository).should(times(1)).terminateGame(eq(1L));
        then(chessRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("게임 재시작에 대한 테스트")
    void restart() {
        willDoNothing().given(chessRepository).restart(eq(1L), any());

        gameService.restart(1L);

        then(chessRepository).should(times(1)).restart(eq(1L), any());
        then(chessRepository).should(times(1)).loadGame(eq(1L));
        then(chessRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모든 게임 찾는 것에 대한 테스트")
    void findAllGames() {
        List<ChessGame> games = Arrays.asList(
                new ChessGame(),
                new ChessGame(),
                new ChessGame());
        given(chessRepository.findAllGames()).willReturn(games);

        assertThat(gameService.findAllGames()).isEqualTo(games);

        then(chessRepository).should(times(1)).findAllGames();
        then(chessRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("새로운 게임 생성에 대한 테스트")
    void newGame() {
        given(chessRepository.addGame(any(), eq("test"))).willReturn(1L);

        assertThat(gameService.newGame("test")).isEqualTo(1L);

        then(chessRepository).should(times(1)).findGame(eq("test"));
        then(chessRepository).should(times(1)).addGame(any(), eq("test"));
        then(chessRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("중복된 이름의 방이 있을 때에 대한 테스트")
    void wasDuplicateWhenVerifyingDuplicateTitleInGames() {
        given(chessRepository.findGame("test")).willReturn(Optional.of("test"));

        assertThatThrownBy(() -> gameService.verifyDuplicateTitleInGames("test")).isInstanceOf(IllegalArgumentException.class);

        then(chessRepository).should(times(1)).findGame("test");
        then(chessRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("중복된 이름의 방이 없을 때에 대한 테스트")
    void wasNotDuplicateWhenVerifyingDuplicateTitleInGames() {
        given(chessRepository.findGame("test")).willReturn(Optional.empty());

        gameService.verifyDuplicateTitleInGames("test");

        then(chessRepository).should(times(1)).findGame("test");
        then(chessRepository).shouldHaveNoMoreInteractions();
    }
}