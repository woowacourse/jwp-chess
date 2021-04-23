package chess.controller;

import chess.service.RoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ExceptionController.class, RoomRestController.class})
class ExceptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

    @DisplayName("없는 경로를 처리하는 방법을 확인한다.")
    @Test
    public void exceptionHandlerTest() throws Exception {
        final RequestBuilder request = MockMvcRequestBuilders.get("/none");
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @DisplayName("IllegalArgumentException 처리를 확인한다.")
    @Test
    public void IAEHandlerTest() throws Exception {
        final RequestBuilder request = MockMvcRequestBuilders.post("/room/create")
                .param("roomName", "test_name");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
}