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
    private MockMvc mockMvc;

    @Autowired
    private RoomService roomService;

    @DisplayName("유효한 이름으로 방 생성")
    @Test
    public void createRoom() throws Exception {
        final RequestBuilder request = MockMvcRequestBuilders.post("/room/create")
                .param("roomName", "validName");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("유효하지 않은 이름으로 방 생성 (너무 짧은 방 이름)")
    @Test
    public void createRoomWithTooShortName() throws Exception {
        final RequestBuilder request = MockMvcRequestBuilders.post("/room/create")
                .param("roomName", "aa");

        mockMvc.perform(request)
                .andExpect(status().is4xxClientError());
    }


    @DisplayName("유효하지 않은 이름으로 방 생성 (너무 긴 방 이름)")
    @Test
    public void createRoomWithTooLongName() throws Exception {
        final RequestBuilder request = MockMvcRequestBuilders.post("/room/create")
                .param("roomName", "aaaaaaaaaaaaaaaaaa");

        mockMvc.perform(request)
                .andExpect(status().is4xxClientError());
    }

    @DisplayName("방 삭제 테스트")
    @Test
    public void deleteRoom() throws Exception {
        final long roomId = roomService.save("newRoom");
        final int preSize = roomService.loadList().size();

        final RequestBuilder request = MockMvcRequestBuilders.delete("/room/"+ roomId);
        mockMvc.perform(request)
                .andExpect(status().isOk());

        assertThat(preSize-1)
                .isEqualTo(roomService.loadList().size());
    }

    @DisplayName("중복 방 이름 생성 시 404 응답")
    @Test
    public void duplicatedRoom() throws Exception {
        final RequestBuilder request = MockMvcRequestBuilders.post("/room/create")
                .param("roomName", "duplicated");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
}