package chess.service;

import chess.dao.FakeBoardDao;
import chess.dao.FakeRoomDao;
import chess.domain.Team;

import chess.dto.GameIdDto;
import chess.dto.MakeRoomDto;
import chess.dto.MoveDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChessServiceTest {

    private ChessService chessService;
    private FakeRoomDao fakeRoomDao;

    @BeforeEach
    void setUp() {
        fakeRoomDao = new FakeRoomDao();
        chessService = new ChessService(new FakeBoardDao(), fakeRoomDao);
        chessService.makeGame(new MakeRoomDto("lawn", "1"));
    }

    @Test
    @DisplayName("방 생성")
    void makeRoom() {
        assertThat(chessService.makeGame(new MakeRoomDto("green", "1234")))
                .isNotNull();
    }

    @Test
    @DisplayName("기물 move 확인")
    void move() {
        Long id = chessService.makeGame(new MakeRoomDto("lawn", "2"));
        assertThat(chessService.move(id, new MoveDto("a2", "a3")).getGameState())
                .isEqualTo(Team.BLACK);
    }

    @Test
    @DisplayName("game 삭제 확인")
    void endGame() {
        Long id = chessService.makeGame(new MakeRoomDto("lawn", "2"));
        chessService.endGame(id);
        assertThat(fakeRoomDao.findById(new GameIdDto(id))).isNull();
    }

    @Test
    @DisplayName("게임 종료시 상태를 NONE으로 변경")
    void endStatus() {
        Long id = chessService.makeGame(new MakeRoomDto("lawn", "2"));
        chessService.updateEndStatus(id);
        assertThat(fakeRoomDao.findById(new GameIdDto(id)).getStatus()).isEqualTo(Team.NONE);
    }

    @Test
    @DisplayName("삭제할 수 있는지 확인")
    void isPossibleDelete() {
        Long id = chessService.makeGame(new MakeRoomDto("lawn lawn", "2"));
        chessService.updateEndStatus(id);
        assertThat(chessService.isPossibleDeleteGame(id, "2")).isTrue();
    }

    @Test
    @DisplayName("체스 게임 우승자 확인 - 무승부")
    void checkGameWinner() {
        assertThat(chessService.findWinner().getGameState()).isEqualTo(Team.NONE);
    }
}
