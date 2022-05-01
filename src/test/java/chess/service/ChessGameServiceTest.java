package chess.service;

import chess.controller.request.RoomCreationRequest;
import chess.dao.FakeGameDao;
import chess.dao.FakePieceDao;
import chess.serviece.ChessGameService;
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
        RoomCreationRequest roomCreationRequest = new RoomCreationRequest();
        String title = "라라";
        String password = "1234";
        roomCreationRequest.setTitle(title);
        roomCreationRequest.setPassword(password);

        // when
        Long id = chessService.addGame(roomCreationRequest);

        // then
        assertAll(
                () -> assertThat(chessService.getGame(id).getTitle()).isEqualTo(title),
                () -> assertThat(chessService.getGame(id).getPassword()).isEqualTo(password)
        );
    }

    @Test
    @DisplayName("게임 검색")
    void getGame() {
        // given
        RoomCreationRequest roomCreationRequest = new RoomCreationRequest();
        String title = "라라";
        String password = "1234";
        roomCreationRequest.setTitle(title);
        roomCreationRequest.setPassword(password);
        Long id = chessService.addGame(roomCreationRequest);

        // then
        assertAll(
                () -> assertThat(chessService.getGame(id).getTitle()).isEqualTo(title),
                () -> assertThat(chessService.getGame(id).getPassword()).isEqualTo(password)
        );
    }

    @Test
    @DisplayName("게임 검색")
    void getAllGame() {
        // given
        RoomCreationRequest roomCreationRequest1 = new RoomCreationRequest();
        roomCreationRequest1.setTitle("라라");
        roomCreationRequest1.setPassword("1234");
        chessService.addGame(roomCreationRequest1);

        RoomCreationRequest roomCreationRequest2 = new RoomCreationRequest();
        roomCreationRequest2.setTitle("루루");
        roomCreationRequest2.setPassword("1234");
        chessService.addGame(roomCreationRequest2);

        // then
        assertThat(chessService.getAllGames().size()).isEqualTo(2);
    }
}
