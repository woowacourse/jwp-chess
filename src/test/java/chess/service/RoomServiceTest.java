package chess.service;

import static chess.service.RoomServiceMessage.ROOM_DELETE_SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.FakeRoomDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RoomServiceTest {

    @ParameterizedTest
    @DisplayName("새 게임 생성에 성공한다면 성공 안내문을 반환하고, 실패 한다면 실패 이유 안내문을 반환한다.")
    @CsvSource({"first, ROOM_CREATE_FAIL_BY_DUPLICATED_NAME", "second, ROOM_CREATE_SUCCESS"})
    void saveNewRoom(final String roomName, final RoomServiceMessage expected) {
        //given
        final RoomService roomService = new RoomService(new FakeRoomDao());
        roomService.saveNewRoom("first", "1234");

        //when
        final RoomServiceMessage actual = roomService.saveNewRoom(roomName, "5678");

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("체스방 삭제에 성공한다면 성공 안내문을 반환하고, 실패 한다면 실패 이유 안내문을 반환한다.")
    @CsvSource({"1234, ROOM_DELETE_FAIL_BY_NOT_END_GAME", "4567, ROOM_DELETE_FAIL_BY_WRONG_PASSWORD"})
    void deleteRoom_fail(final String password, final RoomServiceMessage expected) {
        //given
        final FakeRoomDao roomDao = new FakeRoomDao();
        final RoomService roomService = new RoomService(roomDao);
        final String roomName = "first";
        roomService.saveNewRoom(roomName, "1234");

        //when
        final RoomServiceMessage actual = roomService.deleteRoom(roomName, password);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("일치하는 비밀번호로 게임이 종료된 체스방을 삭제하면 성공 메시지를 반환한다.")
    void deleteRoom_success() {
        //given
        final FakeRoomDao roomDao = new FakeRoomDao();
        final RoomService roomService = new RoomService(roomDao);
        final String roomName = "first";
        final String password = "1234";
        roomService.saveNewRoom(roomName, password);
        roomDao.updateGameState(roomName, "end");

        //when
        final RoomServiceMessage actual = roomService.deleteRoom(roomName, password);

        //then
        assertThat(actual).isEqualTo(ROOM_DELETE_SUCCESS);
    }
}
