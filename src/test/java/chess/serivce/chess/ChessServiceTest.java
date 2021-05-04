package chess.serivce.chess;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import chess.domain.dto.move.MoveResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ChessServiceTest {

    private final ChessService chessService;

    public ChessServiceTest(ChessService chessService) {
        this.chessService = chessService;
    }

    @DisplayName("서비스 방 생성 - 성공")
    @Test
    void createRoom_success() {
        // given
        String roomName = "pkroom2";

        // then
        assertThatCode(() -> chessService.createRoom(roomName)).doesNotThrowAnyException();
    }

    @DisplayName("서비스 기물 이동 - 주어진 룸에서 기물을 움직이고 결과를 반환한다.")
    @Test
    void move_success() {
        // given
        chessService.createRoom("pkroom3");

        // when
        MoveResponseDto result = chessService.move("pkroom3", "12", "14");

        // then
        assertThatCode(() -> {
            assertThat(result.getPieces().size()).isEqualTo(32);
        }).doesNotThrowAnyException();
    }


    @DisplayName("서비스 게임 종료 - 성공")
    @Test
    void end_success() {
        // given
        String roomName = "markRoom";
        chessService.createRoom(roomName);
        chessService.move(roomName, "12", "13");

        // when
        MoveResponseDto result = chessService.end(roomName);

        // then
        assertThatCode(() -> {
            assertThat(result.getPieces().size()).isEqualTo(32);
            assertThat(result.getCurrentTeam()).isEqualTo("B");
        }).doesNotThrowAnyException();
    }

    @DisplayName("서비스 게임 체스말 조회 - 성공")
    @Test
    void findPiecesInRoom_success() {
        // given
        String roomName = "markroom2";
        chessService.createRoom(roomName);

        // when
        MoveResponseDto result = chessService.findPiecesInRoom(roomName);

        // then
        assertThatCode(() -> {
            assertThat(result.getPieces().size()).isEqualTo(32);
            assertThat(result.getCurrentTeam()).isEqualTo("W");
        }).doesNotThrowAnyException();
    }
}