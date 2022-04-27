package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.dao.FakeRoomDao;
import chess.dto.RoomDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoomServiceTest {

    private final FakeRoomDao roomDao = new FakeRoomDao();
    private final RoomService roomService = new RoomService(roomDao);

    @BeforeEach
    void setUp() {
        roomService.saveNewRoom("first", "1234");
    }

    @Test
    @DisplayName("생성하려는 방의 이름이 이미 존재한다면 예외를 발생시킨다.")
    void saveNewRoomExceptionByDuplicatedName() {
        //given
        final String passWord = "5678";
        final String duplicatedName = "first";

        //when then
        assertThatThrownBy(() -> roomService.saveNewRoom(duplicatedName, passWord))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 동일한 이름의 체스방이 존재합니다.");
    }

    @Test
    @DisplayName("일치하는 비밀번호로 게임이 종료된 체스방을 삭제할 수 있다..")
    void deleteRoom_success() {
        //given
        final int roomId = 1;
        roomDao.saveGameState(roomId, "end");

        final String newRoomName = "first";
        final String password = "1234";

        //when
        roomService.deleteRoom(roomId, password);

        //then
        assertThatNoException()
                .isThrownBy(() -> roomService.saveNewRoom(newRoomName, password));
    }

    @Test
    @DisplayName("잘못된 비밀번호를 가지고 체스방을 삭제하려고 하면 예외를 발생시킨다.")
    void deleteRoomExceptionByIncorrectPassword() {
        //given
        final int roomId = 1;
        final String incorrectPassword = "5678";

        //when then
        assertThatThrownBy(() -> roomService.deleteRoom(roomId, incorrectPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("게임이 진행 중인 체스방을 삭제하려고 하면 예외를 발생시킨다.")
    void deleteRoomExceptionByNotEndGame() {
        //given
        final int roomId = 1;
        final String passWord = "1234";
        roomDao.saveGameState(roomId, "playing");
        //when then
        assertThatThrownBy(() -> roomService.deleteRoom(roomId, passWord))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("게임이 진행중인 체스방은 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("방들의 이름을 반환한다.")
    void getRoomNames() {
        //given
        roomDao.saveNewRoom("second", "1234");
        roomDao.saveNewRoom("third", "1234");

        //when
        final List<RoomDto> roomNames = roomDao.getRoomNames();

        //then
        assertAll(
                () -> assertThat(roomNames.get(0).getRoomName()).isEqualTo("first"),
                () -> assertThat(roomNames.get(1).getRoomName()).isEqualTo("second"),
                () -> assertThat(roomNames.get(2).getRoomName()).isEqualTo("third"),
                () -> assertThat(roomNames.get(0).getRoomId()).isEqualTo(1),
                () -> assertThat(roomNames.get(1).getRoomId()).isEqualTo(2),
                () -> assertThat(roomNames.get(2).getRoomId()).isEqualTo(3)
        );
    }
}
