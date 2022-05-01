package chess.service;

import chess.dao.FakeGameDao;
import chess.dao.FakePieceDao;
import chess.dto.GameDto;
import chess.serviece.ChessGameService;
import chess.dto.GameCreationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ChessGameServiceTest {

    private ChessGameService chessService;

    @BeforeEach
    void setUp() {
        chessService = new ChessGameService(new FakeGameDao(), new FakePieceDao());
    }

    @Test
    @DisplayName("게임 추가")
    void addGame() {
        // given
        GameCreationRequest gameCreationRequest = new GameCreationRequest();
        String title = "라라";
        String password = "1234";
        gameCreationRequest.setTitle(title);
        gameCreationRequest.setPassword(password);

        // when
        Long id = chessService.addGame(gameCreationRequest);

        // then
        assertAll(
                () -> assertThat(chessService.getGame(id, new GameDto(id, "1234")).getTitle()).isEqualTo(title),
                () -> assertThat(chessService.getGame(id, new GameDto(id, "1234")).getPassword()).isEqualTo(password)
        );
    }

    @Test
    @DisplayName("게임 검색")
    void getGame() {
        // given
        GameCreationRequest gameCreationRequest = new GameCreationRequest();
        String title = "라라";
        String password = "1234";
        gameCreationRequest.setTitle(title);
        gameCreationRequest.setPassword(password);
        Long id = chessService.addGame(gameCreationRequest);

        // then
        assertAll(
                () -> assertThat(chessService.getGame(id, new GameDto(id, "1234")).getTitle()).isEqualTo(title),
                () -> assertThat(chessService.getGame(id, new GameDto(id, "1234")).getPassword()).isEqualTo(password)
        );
    }

    @Test
    @DisplayName("게임 검색")
    void getAllGame() {
        // given
        GameCreationRequest gameCreationRequest1 = new GameCreationRequest();
        gameCreationRequest1.setTitle("라라");
        gameCreationRequest1.setPassword("1234");
        chessService.addGame(gameCreationRequest1);

        GameCreationRequest gameCreationRequest2 = new GameCreationRequest();
        gameCreationRequest2.setTitle("루루");
        gameCreationRequest2.setPassword("1234");
        chessService.addGame(gameCreationRequest2);

        // then
        assertThat(chessService.getAllGames().size()).isEqualTo(2);
    }
}
