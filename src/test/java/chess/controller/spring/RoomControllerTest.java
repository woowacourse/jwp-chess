package chess.controller.spring;

import chess.dto.CommonDto;
import chess.dto.RoomDto;
import chess.dto.RoomListDto;
import chess.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RoomController.class)
class RoomControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    RoomService roomService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void 방_목록을_불러온다() throws Exception {
        // given
        RoomListDto roomListDto = new RoomListDto(Arrays.asList(
                new RoomDto(1, "1번 방"),
                new RoomDto(2, "2번 방"),
                new RoomDto(3, "3번 방"))
        );
        final CommonDto<RoomListDto> commonDto = new CommonDto<>("게임 목록을 불러왔습니다.", roomListDto);
        given(roomService.list()).willReturn(commonDto);

        // when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
                .get("/room/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(commonDto)));

        // then
        actions.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("message").value("게임 목록을 불러왔습니다."))
                .andExpect(jsonPath("item.rooms[0].gameId").value(1))
                .andExpect(jsonPath("item.rooms[0].name").value("1번 방"))
                .andExpect(jsonPath("item.rooms[1].gameId").value(2))
                .andExpect(jsonPath("item.rooms[1].name").value("2번 방"))
                .andExpect(jsonPath("item.rooms[2].gameId").value(3))
                .andExpect(jsonPath("item.rooms[2].name").value("3번 방"));
    }

    @Test
    void 게임을_저장한다() throws Exception {
        // given
        RoomDto roomDto = new RoomDto(1, "1번 방");
        final CommonDto<RoomDto> commonDto = new CommonDto<>("방 정보를 가져왔습니다.", roomDto);
        given(roomService.save(roomDto)).willReturn(commonDto);

        // when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
                .post("/room/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(commonDto)));

        // then
        actions.andExpect(status().is2xxSuccessful());
    }

    @Test
    void 방_이름을_불러온다() throws Exception {
        // given
        given(roomService.loadRoomName(1)).willReturn("1번 방");

        // when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
                .get("/room/1/load"));

        // then
        actions.andExpect(status().is2xxSuccessful())
                .andExpect(content().string("1번 방"));
    }
}