package chess.service;

import chess.model.dao.GameDao;
import chess.model.dao.PieceDao;
import chess.model.dto.MoveDto;
import chess.model.dto.RoomDto;
import chess.model.dto.WebBoardDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class ChessServiceTest {

    @Autowired
    private GameDao gameDao;
    @Autowired
    private PieceDao pieceDao;

    private ChessService chessService;

    @BeforeEach
    void setUp() {
        chessService = new ChessService(pieceDao, gameDao);
        pieceDao.deleteAll();
        gameDao.deleteAll();
    }


    @Test
    @DisplayName("게임 시작 시 보드를 생성하는지 확인")
    void startTest() {
        Long gameId = chessService.start(new RoomDto("room1", "1234"));
        WebBoardDto board = chessService.getBoardByGameId(gameId);

        assertThat(board.getWebBoard().size()).isEqualTo(64);
    }

    @Test
    @DisplayName("게임 리스트를 올바르게 반환하는지 확인한다.")
    void getAllGamesTest() {
        chessService.start(new RoomDto("room1", "1234"));
        chessService.start(new RoomDto("room2", "1234"));
        chessService.start(new RoomDto("room3", "1234"));

        assertThat(chessService.getAllGames().getGameInfos().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("체스 말이 올바르게 이동했는지 확인한다.")
    void moveTest() {
        Long gameId = chessService.start(new RoomDto("room1", "1234"));
        WebBoardDto boardDto = chessService.move(new MoveDto("a2", "a4"), gameId);

        assertAll(() -> {
            assertThat(boardDto.getWebBoard().get("a2")).isEqualTo("none-.");
            assertThat(boardDto.getWebBoard().get("a4")).isEqualTo("white-p");
        });
    }

    @Test
    @DisplayName("처음 가져온 턴의 상태가 화이트인지 확인한다.")
    void turnTest() {
        Long gameId = chessService.start(new RoomDto("room1", "1234"));

        assertThat(chessService.getTurn(gameId)).isEqualToIgnoringCase("white");
    }
}
