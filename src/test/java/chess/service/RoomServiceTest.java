package chess.service;

import chess.dao.RoomDao;
import chess.dto.CommonDto;
import chess.dto.RoomDto;
import chess.dto.RoomListDto;
import chess.exception.HandledException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @InjectMocks
    RoomService roomService;

    @Mock
    RoomDao roomDao;

    @Test
    void 방_이름을_불러온다() {
        // given
        List<RoomDto> roomDtoList = Arrays.asList(
                new RoomDto(1, "1번 방"),
                new RoomDto(2, "2번 방"),
                new RoomDto(3, "3번 방"));

        // when
        given(roomDao.loadRoomList()).willReturn(roomDtoList);

        // then
        CommonDto<RoomListDto> roomListDto = roomService.list();
        assertThat(roomListDto.getMessage()).isEqualTo("게임 목록을 불러왔습니다.");
        assertThat(roomListDto.getItem().getRooms()).containsAll(roomDtoList);
    }

    @Test
    void 방_정보를_저장한다() {
        // given
        RoomDto roomDto = new RoomDto(1, "홍철 없는 홍철 팀");

        // when
        given(roomDao.countRoomByName("홍철 없는 홍철 팀")).willReturn(0);

        // then
        CommonDto<RoomDto> commonDto = roomService.save(roomDto);
        assertThat(commonDto.getMessage()).isEqualTo("방 정보를 가져왔습니다.");
        assertThat(commonDto.getItem().getGameId()).isEqualTo(1);
        assertThat(commonDto.getItem().getName()).isEqualTo("홍철 없는 홍철 팀");
    }

    @Test
    void 존재하는_방_이름인_경우_저장할_수_없다() {
        // given
        RoomDto roomDto = new RoomDto(1, "홍철 없는 홍철 팀");

        // when
        given(roomDao.countRoomByName("홍철 없는 홍철 팀")).willReturn(1);

        // then
        assertThatThrownBy(() -> roomService.save(roomDto))
                .isInstanceOf(HandledException.class)
                .hasMessage("방이 이미 등록되어있습니다.");
    }

    @Test
    void 저장되어있는_방을_불러온다() {
        // given
        int gameId = 1;

        // when
        given(roomDao.loadRoomName(gameId)).willReturn("1번 방");

        // then
        assertThat(roomService.loadRoomName(gameId)).isEqualTo("1번 방");
    }
}