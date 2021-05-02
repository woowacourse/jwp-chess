package chess.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

import chess.domain.ChessGame;
import chess.domain.dto.GameDto;
import chess.domain.position.Position;
import chess.domain.web.Game;
import chess.domain.web.GameHistory;
import chess.repository.GameDao;
import chess.repository.GameHistoryDao;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChessServiceTest {

    @InjectMocks
    private ChessService chessService;

    @Mock
    private GameDao gameDao;
    @Mock
    private GameHistoryDao gameHistoryDao;

    @Test
    @DisplayName("Game 추가 테스트")
    public void addGameTest() {
        Game game = new Game(1, false, LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        given(gameDao.addGame(any())).willReturn(1);

        int addedGameId = chessService.getAddedGameId(game);

        assertThat(addedGameId).isEqualTo(1);
    }

    @Test
    @DisplayName("유저 아이디로 게임 찾기 테스트")
    public void findGameByUserIdTest() {
        given(gameDao.findGamesByUserId(anyInt())).willReturn(new ArrayList<>());

        List<GameDto> gamesByUserId = chessService.findGamesByUserId(1);

        assertThat(gamesByUserId.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("게임 히스토리 복구 테스트")
    public void reloadAllHistoryTest() {
        List<GameHistory> gameHistories = new ArrayList<>();
        gameHistories
            .add(new GameHistory(1, "move a2 a4", LocalDateTime.now(ZoneId.of("Asia/Seoul"))));
        given(gameHistoryDao.findAllGameHistoryByGameId(anyInt())).willReturn(gameHistories);

        ChessGame chessGame = chessService.reloadAllHistory(1);

        assertThat(chessGame.getBoard().hasPieceAt(Position.of("a4"))).isTrue();
    }
}