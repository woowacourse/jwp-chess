package chess.controller.spring;

import chess.controller.spring.vo.Pagination;
import chess.controller.spring.vo.SessionVO;
import chess.dto.room.RoomDTO;
import chess.dto.room.RoomRegistrationDTO;
import chess.dto.room.RoomResponseDTO;
import chess.service.spring.RoomService;
import chess.service.spring.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext
@ActiveProfiles("test")
class RoomControllerTest {

    private int firstRoomId;
    private int secondRoomId;

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
        firstRoomId = roomService.addRoom("room1", "pass1");
        secondRoomId = roomService.addRoom("room2", "pass1");
    }

    @AfterEach
    void tearDown() {
        userService.deleteAllByRoomId(firstRoomId);
        userService.deleteAllByRoomId(secondRoomId);

        roomService.deleteById(firstRoomId);
        roomService.deleteById(secondRoomId);
    }

    @DisplayName("방 목록을 조회한다.")
    @Test
    void findAllRooms() throws Exception {
        List<RoomDTO> roomDTOS = Arrays.asList(new RoomDTO(firstRoomId, "room1"), new RoomDTO(secondRoomId, "room2"));
        Pagination pagination = new Pagination(1, 2);
        String expectedResponseBody = writeResponseBody(new RoomResponseDTO(roomDTOS, pagination));

        mockMvc.perform(get("/rooms").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponseBody));
    }

    private String writeResponseBody(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    @DisplayName("방을 추가한다.")
    @Test
    void addRoom() throws Exception {
        String requestBody = writeResponseBody(new RoomRegistrationDTO("room3", "pass1"));
        String expectedResponseBody = writeResponseBody(new RoomDTO(secondRoomId + 1, "room3"));

        mockMvc.perform(post("/rooms")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponseBody));

        userService.deleteAllByRoomId(secondRoomId + 1);
        roomService.deleteById(secondRoomId + 1);
    }

    @DisplayName("세션이 존재하면 방을 추가할 수 없다.")
    @Test
    void cannotAddRoom() throws Exception {
        String requestBody = writeResponseBody(new RoomRegistrationDTO("room3", "pass1"));

        mockMvc.perform(post("/rooms")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody)
                .sessionAttr("session", new SessionVO(1, "pass31")))
                .andExpect(status().isBadRequest());
    }
}
