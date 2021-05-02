package chess.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chess.controller.dto.RoomRequestDto;
import chess.dao.room.RoomDao;
import chess.domain.room.Room;
import chess.domain.room.RoomInformation;
import chess.websocket.commander.dto.EnterRoomRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"http", "test"})
class RoomApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void after() {
        roomDao.removeAll();
    }

    @Test
    @DisplayName("locked 지만 패스워드가 존재하지 않을 경우")
    void createRoom_locked_wrongPassword() throws Exception {
        final RoomRequestDto roomRequestDto = new RoomRequestDto("test", true, "", "nabom");
        mockMvc.perform(
            post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomRequestDto))
        )
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("locked고 패스워드가 잘 적혀있을 경우")
    void createRoom_locked_correctPassword() throws Exception {
        final RoomRequestDto roomRequestDto = new RoomRequestDto("test", true, "123", "nabom");
        mockMvc.perform(
            post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomRequestDto))
        )
            .andDo(print())
            .andExpect(status().isCreated());
        // 리다이렉트 url을 테스트하고 싶은데 너무 주소와 숫자에 의존적?
//            .andExpect(redirectedUrl("http://localhost/rooms/1"));
    }

    @Test
    @DisplayName("unlocked 룸 만들기")
    void createRoom_unlocked() throws Exception {
        final RoomRequestDto roomRequestDto = new RoomRequestDto("test", false, "", "nabom");
        mockMvc.perform(
            post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomRequestDto))
        )
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("없는 룸 찾기")
    void findRoom_notExist() throws Exception {
        mockMvc.perform(get("/rooms/-100"))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("룸 찾기 성공")
    void findRoom_exist() throws Exception {
        final Room createdRoom = roomDao.newRoom(new RoomInformation("test", false, ""));
        mockMvc.perform(get("/rooms/" + createdRoom.id())
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("roomId").value(createdRoom.id()))
            .andExpect(jsonPath("title").value(createdRoom.title()))
            .andExpect(jsonPath("locked").value(createdRoom.isLocked()))
            .andExpect(jsonPath("_links.self").exists());
    }

    @Test
    @DisplayName("룸 리스트 받기")
    void getRoomList() throws Exception {
        final Room room1 = roomDao.newRoom(new RoomInformation("test1", false, ""));
        final Room room2 = roomDao.newRoom(new RoomInformation("test2", false, ""));
        mockMvc.perform(get("/rooms"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].roomId").value(room1.id()))
            .andExpect(jsonPath("$[1].roomId").value(room2.id()));
    }

    @Test
    @DisplayName("플레이어로 방 입장_잘못된 비밀번호")
    void enterRoomAsPlayer_incorrectPassword() throws Exception {
        Room createdRoom =
            roomDao.newRoom(new RoomInformation("test1", true, "1234"));
        EnterRoomRequestDto enterRoom =
            new EnterRoomRequestDto(createdRoom.id(), "nabom", "12345");

        mockMvc.perform(put("/rooms/enter/player")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(enterRoom))
        )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().string("비밀번호가 틀렸습니다."));
    }

    @Test
    @DisplayName("플레이어로 방 입장")
    void enterRoomAsPlayer_success() throws Exception {
        Room createdRoom =
            roomDao.newRoom(new RoomInformation("test1", true, "1234"));
        EnterRoomRequestDto enterRoom =
            new EnterRoomRequestDto(createdRoom.id(), "nabom", "1234");

        mockMvc.perform(put("/rooms/enter/player")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(enterRoom))
        )
            .andDo(print())
            .andExpect(status().isOk());

        //room에 user가 proxy로 들어가게됨..? (아마 빈을 세션 스코프로 잡으면 생기는 문제 같음)
    }

    @Test
    @DisplayName("관전자로 방 입장")
    void enterRoomAsParticipant_success() throws Exception {
        Room createdRoom =
            roomDao.newRoom(new RoomInformation("test1", true, "1234"));
        EnterRoomRequestDto enterRoom =
            new EnterRoomRequestDto(createdRoom.id(), "nabom", "1234");

        mockMvc.perform(put("/rooms/enter/player")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(enterRoom))
        )
            .andDo(print())
            .andExpect(status().isOk());
    }
}