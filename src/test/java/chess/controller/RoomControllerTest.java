package chess.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class RoomControllerTest {

    @Autowired
    MockMvc mockMvc;

    @DisplayName("유효한 이름으로 방 생성")
    @Test
    void createRoom() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/room/create")
                .param("roomName", "validName");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("유효하지 않은 이름으로 방 생성")
    @Test
    void createRoomWithInvalidName() throws Exception {
        RequestBuilder tooShortRoomNameRequest = MockMvcRequestBuilders.post("/room/create")
                .param("roomName", "aa");

        mockMvc.perform(tooShortRoomNameRequest)
                .andExpect(status().is4xxClientError());

        RequestBuilder tooLongRoomNameRequest = MockMvcRequestBuilders.post("/room/create")
                .param("roomName", "aaaaaaaaaaaaaaaaaa");

        mockMvc.perform(tooLongRoomNameRequest)
                .andExpect(status().is4xxClientError());
    }
}