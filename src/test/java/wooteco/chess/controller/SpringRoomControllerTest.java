package wooteco.chess.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.chess.dto.AuthorizeDto;
import wooteco.chess.dto.RoomRequestDto;
import wooteco.chess.repository.entity.RoomEntity;
import wooteco.chess.service.SpringRoomService;





@SpringBootTest
@AutoConfigureMockMvc
public class SpringRoomControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringRoomController springRoomController;

    @Autowired
    private SpringRoomService springRoomService;

    private final RoomRequestDto roomRequestDto = new RoomRequestDto("hello", "world");

    @DisplayName("SpringRoomController가 빈으로 등록됐는지 확인")
    @Test
    void testSpringRoomControllerIsRegisteredBean() {
        assertThat(springRoomController).isNotNull();
    }

    // TODO: 2020/05/04 생각해보면 rooms가 아예 없을 때는 어떻게 하지? 존재하냐 안 하냐로 판단할 수가 없지 않나?
    @DisplayName("rooms로 들어갔을 때 rooms 속성이 잘 들어갔는지, view가 잘 보이는 지 확인")
    @Test
    void testGetAllRooms() throws Exception {
        mockMvc.perform(get("/rooms"))
            .andExpect(status().isOk())
            .andExpect(view().name("index"))
            .andExpect(model().attributeExists("rooms"));
    }

    @DisplayName("랜덤 번호의 방으로 들어갔을 때 id값과 view가 잘 보이는 지 확인")
    @Test
    void testEnterRoom() throws Exception {
        RoomEntity roomEntity = springRoomService.addRoom(roomRequestDto);
        Long id = roomEntity.getId();

        mockMvc.perform(get("/rooms/enter/" + id))
            .andExpect(status().isOk())
            .andExpect(view().name("game"))
            .andExpect(model().attribute("id", id));
    }

    @DisplayName("roomRequestDto로 방을 만드는 지 확인")
    @Test
    void testCreateRoom() throws Exception {
        mockMvc.perform(post("/rooms/create")
            .flashAttr("roomRequestDto", roomRequestDto))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/rooms"));
    }

    @DisplayName("방을 삭제하고 난 뒤에 응답으로부터 200이 오는지 확인")
    @Test
    void testRemoveRoom() throws Exception {
        RoomEntity roomEntity = springRoomService.addRoom(roomRequestDto);
        Long id = roomEntity.getId();

        mockMvc.perform(delete("/rooms/remove/" + id))
            .andExpect(status().isOk());
    }

    @DisplayName("방을 만들고 해당 비밀번호로 접근해 true가 응답으로 오는지 확인ㄴ")
    @Test
    void testAuthorize() throws Exception {
        RoomEntity roomEntity = springRoomService.addRoom(roomRequestDto);
        AuthorizeDto authorizeDto = new AuthorizeDto(roomEntity.getId(), roomEntity.getPassword());

        mockMvc.perform(post("/rooms/authorize")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(authorizeDto)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string("true"));
    }
}
