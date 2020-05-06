package chess.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chess.dto.CreateRoomDto;
import chess.dto.DeleteRoomDto;
import chess.dto.RoomsDto;
import chess.model.domain.piece.Team;
import chess.model.repository.RoomEntity;
import chess.service.ChessGameService;
import chess.service.RoomService;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RoomApiControllerTest {

    private static final Gson GSON = new Gson();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChessGameService chessGameService;
    @Autowired
    private RoomService roomService;

    Map<Team, String> userNames = new HashMap<>();
    RoomEntity room;

    @BeforeEach
    void setUp() {
        userNames.put(Team.BLACK, "BLACK");
        userNames.put(Team.WHITE, "WHITE");
        room = roomService.addRoom(new CreateRoomDto("test", ""));
        chessGameService.saveNewUserNames(userNames);
    }

    @Test
    void viewRooms() throws Exception {
        Map<Integer, String> temp = new HashMap<>();
        temp.put(room.getId(), "test");
        RoomsDto expected = new RoomsDto(temp);

        mockMvc.perform(get("/api/rooms"))
            .andExpect(status().isOk())
            .andExpect(content().json(GSON.toJson(expected)));
    }

    @Test
    void createRoom() throws Exception {
        String request = GSON.toJson(new CreateRoomDto("test2", ""));

        Map<Integer, String> temp
            = new HashMap<>(roomService.getUsedRooms().getRooms());
        temp.put(room.getId() + 1, "test2");
        RoomsDto expected = new RoomsDto(temp);

        //실행 과정 검사 및 결과 반환
        MvcResult result = mockMvc.perform(post("/api/room")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
            .andExpect(status().isOk())
            .andReturn();

        //추가 후 최종 방 목록 확인
        assertThat(result.getResponse().getContentAsString())
            .isEqualTo(GSON.toJson(expected));
    }

    @Test
    void deleteRoom() throws Exception {
        RoomEntity testRoom = roomService.addRoom(new CreateRoomDto("test2", ""));
        String body = GSON.toJson(new DeleteRoomDto(testRoom.getId()));

        Map<Integer, String> temp
            = new HashMap<>(roomService.getUsedRooms().getRooms());
        temp.remove(testRoom.getId());
        RoomsDto expected = new RoomsDto(temp);

        //실행 과정 검사 및 결과 반환
        MvcResult result = mockMvc.perform(delete("/api/room")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().isOk())
            .andReturn();

        //삭제 후 최종 방 목록 확인
        assertThat(result.getResponse().getContentAsString())
            .isEqualTo(GSON.toJson(expected));
    }
}