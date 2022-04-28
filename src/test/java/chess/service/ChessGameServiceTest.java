package chess.service;

import chess.dao.FakeJdbcGameDao;
import chess.dao.FakePieceDao;
import chess.serviece.ChessGameService;
import chess.serviece.dto.GameCreationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ChessGameServiceTest {

    private ChessGameService chessService;

    @BeforeEach
    void setUp() {
        chessService = new ChessGameService(new FakeJdbcGameDao(), new FakePieceDao());
    }

    @Test
    @DisplayName("게임 추가")
    void addGame() {
        // given
        GameCreationDto gameCreationDto = new GameCreationDto();
        String title = "라라";
        String password = "1234";
        gameCreationDto.setTitle(title);
        gameCreationDto.setPassword(password);

        // when
        Long id = chessService.addGame(gameCreationDto);

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
        GameCreationDto gameCreationDto = new GameCreationDto();
        String title = "라라";
        String password = "1234";
        gameCreationDto.setTitle(title);
        gameCreationDto.setPassword(password);
        Long id = chessService.addGame(gameCreationDto);

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
        GameCreationDto gameCreationDto1 = new GameCreationDto();
        gameCreationDto1.setTitle("라라");
        gameCreationDto1.setPassword("1234");
        chessService.addGame(gameCreationDto1);

        GameCreationDto gameCreationDto2 = new GameCreationDto();
        gameCreationDto2.setTitle("루루");
        gameCreationDto2.setPassword("1234");
        chessService.addGame(gameCreationDto2);

        // then
        assertThat(chessService.getAllGames().size()).isEqualTo(2);
    }
}
