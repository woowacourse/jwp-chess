package chess.controller;

import chess.service.ChessService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChessApiController.class)
public class ChessApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChessService chessService;

    @DisplayName("방 이름으로 id 가져오는 테스트")
    @Test
    void getRoomIdTest() throws Exception {
        given(chessService.getRoomId("room1"))
                .willReturn(1);

        MvcResult result = mockMvc.perform(get("/room/room1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("1", result.getResponse().getContentAsString());
    }

    @DisplayName("방 이름 목록 가져오는 테스트")
    @Test
    void getRoomNamesTest() throws Exception {
        given(chessService.getRoomNames())
                .willReturn(
                        new HashMap<Integer, String>() {{
                            put(1, "room1");
                            put(2, "room2");
                            put(3, "room3");
                        }}
                );

        mockMvc.perform(get("/rooms").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json("{\"roomNames\":{\"1\":\"room1\",\"2\":\"room2\",\"3\":\"room3\"}}"));
    }
}
