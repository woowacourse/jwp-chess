package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.FakeRoomDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RoomServiceTest {

    @ParameterizedTest
    @DisplayName("새 게임 생성에 성공한다면 성공 안내문을 반환하고, 실패 한다면 실패 이유 안내문을 반환한다.")
    @CsvSource({"first, ROOM_CREATE_FAIL_BY_DUPLICATED_NAME", "second, ROOM_CREATE_SUCCESS"})
    void isDuplicated(final String roomName, final RoomServiceMessage expected) {
        //given
        final RoomService roomService = new RoomService(new FakeRoomDao());
        roomService.saveNewRoom("first", "1234");

        //when
        final RoomServiceMessage actual = roomService.saveNewRoom(roomName, "5678");

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
