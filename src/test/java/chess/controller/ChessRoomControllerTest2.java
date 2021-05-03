package chess.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chess.domain.dto.RoomDto;
import chess.domain.dto.RoomsDto;
import chess.serivce.chess.ChessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(ChessRoomController.class)
class ChessRoomControllerTest2 {

    @MockBean
    ChessService service;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    void createRoom_success() throws Exception {
        // given
        RoomDto roomDto = new RoomDto("pkRoom");

        // when
        ResultActions perform = this.mockMvc.perform(post("/api/room")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(roomDto)));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("name").exists());
    }

    @Test
    void showRooms_success() throws Exception {
        // given
        RoomsDto roomsDto = new RoomsDto();
        given(service.findAllRooms()).willReturn(roomsDto);

        // when
        ResultActions perform = this.mockMvc.perform(get("/api/room/all")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("rooms").isEmpty());
    }
}
