package chess.service;

import chess.dao.FakeBoardDao;
import chess.dao.FakeRoomDao;
import chess.domain.Team;

import chess.dto.request.GameIdRequest;
import chess.dto.request.RoomRequest;
import chess.dto.response.MoveResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChessServiceTest {

    private ChessService chessService;
    private FakeRoomDao fakeRoomDao;

    @BeforeEach
    void setUp() {
        fakeRoomDao = new FakeRoomDao();
        chessService = new ChessService(new FakeBoardDao(), fakeRoomDao);
        chessService.initializeGame(new RoomRequest("lawn", "1"));
    }

    @Test
    @DisplayName("방 생성")
    void makeRoom() {
        assertThat(chessService.initializeGame(new RoomRequest("green", "1234")))
                .isNotNull();
    }

    @Test
    @DisplayName("기물 move 확인")
    void move() {
        Long id = chessService.initializeGame(new RoomRequest("lawn", "2"));
        assertThat(chessService.move(id, new MoveResponse("a2", "a3")).getGameState())
                .isEqualTo(Team.BLACK);
    }

    @Test
    @DisplayName("game 삭제 확인")
    void endGame() {
        Long id = chessService.initializeGame(new RoomRequest("lawn", "2"));
        chessService.endGame(id);
        assertThat(fakeRoomDao.findById(new GameIdRequest(id))).isNull();
    }

    @Test
    @DisplayName("게임 종료시 상태를 NONE으로 변경")
    void endStatus() {
        Long id = chessService.initializeGame(new RoomRequest("lawn", "2"));
        chessService.updateStateEnd(id);
        assertThat(fakeRoomDao.findById(new GameIdRequest(id)).getStatus()).isEqualTo(Team.NONE);
    }

    @Test
    @DisplayName("삭제할 수 있는지 확인")
    void isPossibleDelete() {
        Long id = chessService.initializeGame(new RoomRequest("lawn lawn", "2"));
        chessService.updateStateEnd(id);
        assertThat(chessService.isPossibleDeleteGame(id, "2")).isTrue();
    }

    @Test
    @DisplayName("체스 게임 우승자 확인 - 무승부")
    void checkGameWinner() {
        Long id = chessService.initializeGame(new RoomRequest("lawn lawn", "2"));
        assertThat(chessService.findWinner(id).getGameState()).isEqualTo(Team.NONE);
    }

    @Test
    @DisplayName("체스 게임 id 존재 여부 확인")
    void isExistGameId() {
        assertThatThrownBy(() ->
                chessService.validateGameId(3))
                .isInstanceOf(SQLException.class)
                .hasMessageContaining("존재하지 않는 게임아이디입니다.");
    }
}
