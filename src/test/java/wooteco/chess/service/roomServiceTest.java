package wooteco.chess.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.dto.RequestDto.RoomCreateRequestDto;
import wooteco.chess.dto.RequestDto.RoomJoinRequestDto;
import wooteco.chess.entity.Room;
import wooteco.chess.repository.RoomRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class roomServiceTest {
    @Autowired
    RoomService roomService;
    @Autowired
    RoomRepository roomRepository;

    @AfterEach
    void deleteAll() {
        roomRepository.deleteAll();
    }

    @DisplayName("create 테스트")
    @Test
    void create() {
        RoomCreateRequestDto requestDto = new RoomCreateRequestDto("whitePassword",
                "createTest");
        assertThat(roomService.create(requestDto)
                .getResponseCode())
                .isEqualTo(200);
    }

    @DisplayName("join 테스트")
    @Test
    void join() {
        RoomCreateRequestDto requestDto = new RoomCreateRequestDto("whitePassword",
                "joinTest");
        roomService.create(requestDto);
        RoomJoinRequestDto requestDto1 = new RoomJoinRequestDto("blackPassword",
                "joinTest");
        assertThat(roomService.join(requestDto1)
                .getResponseCode())
                .isEqualTo(200);
    }

    @DisplayName("checkAuthentication 테스트")
    @Test
    void checkAuthentication() {
        RoomCreateRequestDto requestDto = new RoomCreateRequestDto("whitePassword",
                "authenticationTest");
        Long roomId = (Long) roomService.create(requestDto).getResponseData();
        assertThat(roomService.checkAuthentication(roomId, "whitePassword"))
                .isEqualTo(Team.WHITE);
    }
}
