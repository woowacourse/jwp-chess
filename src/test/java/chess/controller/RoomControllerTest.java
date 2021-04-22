package chess.controller;

import chess.service.RoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class RoomControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RoomService roomService;

    @DisplayName("유효한 이름으로 방 생성")
    @Test
    void createRoom() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/room/create")
                .param("roomName", "validName");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("유효하지 않은 이름으로 방 생성 (너무 짧은 방 이름)")
    @Test
    void createRoomWithTooShortName() throws Exception {
        RequestBuilder tooShortRoomNameRequest = MockMvcRequestBuilders.post("/room/create")
                .param("roomName", "aa");

        mockMvc.perform(tooShortRoomNameRequest)
                .andExpect(status().is4xxClientError());
    }


    @DisplayName("유효하지 않은 이름으로 방 생성 (너무 긴 방 이름)")
    @Test
    void createRoomWithTooLongName() throws Exception {
        RequestBuilder tooLongRoomNameRequest = MockMvcRequestBuilders.post("/room/create")
                .param("roomName", "aaaaaaaaaaaaaaaaaa");

        mockMvc.perform(tooLongRoomNameRequest)
                .andExpect(status().is4xxClientError());
    }

    @DisplayName("방 삭제 테스트")
    @Test
    void deleteRoom() throws Exception {
        long roomId = roomService.save("newRoom");
        int preSize = roomService.loadList().size();

        RequestBuilder deleteRequest = MockMvcRequestBuilders.delete("/room/"+ roomId);
        mockMvc.perform(deleteRequest)
                .andExpect(status().isOk());

        assertThat(preSize-1).isEqualTo(roomService.loadList().size());
    }
}