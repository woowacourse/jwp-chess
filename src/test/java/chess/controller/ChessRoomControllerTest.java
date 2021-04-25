package chess.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.RoomRequestDto;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ChessRoomControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testRoomNameValidation() throws Exception {

        mvc.perform(post("/api/room")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "room")
                .param("pw", "1"))
                .andExpect(status().isBadRequest());
    }
}
