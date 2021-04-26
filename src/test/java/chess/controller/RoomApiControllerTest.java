//package chess.controller;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import chess.controller.dto.RoomRequestDto;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class RoomApiControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    @DisplayName("락이 안 되어있는 방을 생성할 때")
//    void createRoom_noLocked() throws Exception {
//
//        RoomRequestDto roomRequestDto = new RoomRequestDto("test", false, "");
//
//        mockMvc.perform(post("/rooms")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(roomRequestDto))
//        )
//            .andDo(print())
//            .andExpect(status().isCreated());
//    }
//
//    @Test
//    @DisplayName("락이 되어있지만 패스워드를 설정 안 할 경우")
//    void createRoom_locked_noPassword() throws Exception{
//        RoomRequestDto roomRequestDto = new RoomRequestDto("test", true, "");
//
//        mockMvc.perform(post("/rooms")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(roomRequestDto))
//        )
//            .andDo(print())
//            .andExpect(status().isBadRequest())
//            .andExpect(jsonPath("$[0].field").value("password"))
//            .andExpect(jsonPath("$[0].code").value("empty"))
//            .andExpect(jsonPath("$[0].defaultMessage").isNotEmpty());
//    }
//
//    @Test
//    @DisplayName("락이 되어있고 패스워드를 설정 할 경우")
//    void createRoom_locked_withPassword() throws Exception{
//        RoomRequestDto roomRequestDto = new RoomRequestDto("test", true, "123");
//
//        mockMvc.perform(post("/rooms")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(roomRequestDto))
//        )
//            .andDo(print())
//            .andExpect(status().isCreated());
//    }
//
//
//}