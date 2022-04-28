package chess.service;

import chess.dao.FakeJdbcGameDao;
import chess.dao.FakePieceDao;
import chess.serviece.ChessGameService;
import chess.serviece.dto.GameCreationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChessGameServiceTest {

    private ChessGameService chessService;

    @BeforeEach
    void setUp() {
        chessService = new ChessGameService(new FakeJdbcGameDao(), new FakePieceDao());
    }

    @Test
    @DisplayName("게임 검색")
    void addGame() {
        // given
        GameCreationDto gameCreationDto = new GameCreationDto();
        gameCreationDto.setTitle("라라");
        gameCreationDto.setPassword("1234");

        // when
        long id = chessService.addGame(gameCreationDto);

        // then
    }
}
