package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import chess.dao.RoomFakeDao;
import chess.dao.SquareFakeDao;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ChessServiceTests {

    private ChessService chessService = new ChessService(
            new RoomFakeDao(),
            new SquareFakeDao()
    );

    @Test
    @DisplayName("전체 방 리스트를 조회할 수 있다.")
    void findAllRoom() {
        chessService.insertRoom("title1", "1111");
        chessService.insertRoom("title1", "1111");

        assertThat(chessService.findAllRoom()).hasSize(2);
    }

    @Test
    @DisplayName("한개의 방을 생성할 수 있다.")
    void insertRoom() {
        final Long roomId = chessService.insertRoom("title1", "1111");

        assertThat(roomId).isInstanceOf(Long.class);
    }

    @Test
    @DisplayName("방의 일련번호를 통해 보드판과 방의 상태를 변경할 수 있다.")
    void insertBoard() {
        final Long roomId = chessService.insertRoom("title1", "1111");
        final Long updateRoomId = chessService.insertBoard(roomId);

        assertThat(updateRoomId).isInstanceOf(Long.class);
    }

    @Test
    @DisplayName("방의 일련번호를 통해 전체 보드 구성요소를 조회할 수 있다.")
    void findSquareAllById() {
        final Long roomId = chessService.insertRoom("title1", "1111");
        chessService.insertBoard(roomId);

        assertThat(chessService.findSquareAllById(roomId)).hasSize(64);
    }

    @Test
    @DisplayName("방의 일련번호와 현재위치 타겟위치를 통해 보드의 구성요소를 변경할 수 있다.")
    void updateSquares() {
        final Long roomId = chessService.insertRoom("title1", "1111");
        chessService.insertBoard(roomId);

        assertDoesNotThrow(() -> chessService.updateSquares(roomId, "a2", "a3"));
    }

    @Test
    @DisplayName("일련번호를 통해 방의 점수를 조회할 수 있다.")
    void findStatusById() {
        final Long roomId = chessService.insertRoom("title", "1111");
        chessService.insertBoard(roomId);

        final List<Double> status = chessService.findStatusById(roomId);

        assertThat(status).hasSize(2);
    }

    @Test
    @DisplayName("방의 상태를 종료로 변경할 수 있다.")
    void updateStateEnd() {
        final Long roomId = chessService.insertRoom("title1", "1111");
        chessService.insertBoard(roomId);

        assertDoesNotThrow(() -> chessService.updateStateEnd(roomId));
    }

    @Nested
    @DisplayName("방을 삭제하기 위해서 일련번호가 일치하고")
    class deleteRoom {

        @Test
        @DisplayName("비밀번호가 일치하면 삭제된다.")
        void valid() {
            final Long roomId = chessService.insertRoom("title1", "1111");

            assertDoesNotThrow(() -> chessService.deleteRoom(roomId, "1111"));
        }

        @Test
        @DisplayName("비밀번호가 일치히지 않으면 예외가 발생한다.")
        void invalidNotEqualsPassword() {
            final Long roomId = chessService.insertRoom("title1", "1111");

            assertThatThrownBy(() -> chessService.deleteRoom(roomId, "1234"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("비밀번호가 일치하지 않습니다.");
        }

        @Test
        @DisplayName("비밀번호가 일치하지만 방의 상태가 WhiteRunning, BlackRunning일 경우 예외가 발생한다.")
        void invalidState() {
            final Long roomId = chessService.insertRoom("title1", "1111");
            chessService.insertBoard(roomId);

            assertThatThrownBy(() -> chessService.deleteRoom(roomId, "1111"))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("게임이 실행중일 경우 게임을 삭제할 수 없습니다.");
        }
    }
}
