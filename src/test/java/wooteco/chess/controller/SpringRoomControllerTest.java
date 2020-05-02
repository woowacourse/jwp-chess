package wooteco.chess.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.SQLException;

import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.chess.dto.AuthorizeDto;
import wooteco.chess.dto.RoomRequestDto;
import wooteco.chess.exception.WrongIdException;
import wooteco.chess.repository.entity.RoomEntity;
import wooteco.chess.service.SpringGameService;
import wooteco.chess.service.SpringRoomService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SpringRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringRoomService roomService;

    @Autowired
    private ObjectMapper objectMapper;

    private RoomRequestDto ddRoom = new RoomRequestDto("DD", "1234");;

    @Test
    void getAllRooms() throws Exception {
        mockMvc.perform(get("/rooms"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("rooms"))
            .andExpect(view().name("index"));
    }

    @Test
    void enterRoom() throws Exception{
        RoomEntity persistEntity = roomService.addRoom(ddRoom);
        mockMvc.perform(post("/rooms/enter/" + persistEntity.getId())
            .param("loginSuccess", "true"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("roomId", persistEntity.getId()))
        .andExpect(view().name("game"));
    }

    @Test
    void canNotEnterRoom() throws Exception{
        RoomEntity persistEntity = roomService.addRoom(ddRoom);
        mockMvc.perform(post("/rooms/enter/" + persistEntity.getId())
            .param("loginSuccess", "false"))
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("authorizeError", "wrongPassword"))
            .andExpect(redirectedUrl("/rooms"));
    }

    @Test
    void createRoom() throws Exception {
        mockMvc.perform(post("/rooms/create")
            .flashAttr("roomRequestDto", ddRoom))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/rooms"));
    }

    @Test
    void removeRoom() throws Exception {
        RoomEntity persistEntity = roomService.addRoom(ddRoom);
        Long id = persistEntity.getId();
        mockMvc.perform(post("/rooms/remove/"+id).param("loginSuccess", "true"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/rooms"));

        assertThatThrownBy(()->roomService.findById(id))
            .isInstanceOf(WrongIdException.class);
    }

    @Test
    void authorize() throws Exception {
        RoomEntity roomEntity = roomService.addRoom(ddRoom);
        mockMvc.perform(post("/rooms/authorize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AuthorizeDto(roomEntity.getId(),
                    roomEntity.getPassword()))))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("true"));
    }
}