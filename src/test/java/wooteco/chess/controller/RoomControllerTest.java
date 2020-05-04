package wooteco.chess.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.core.StringContains.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import wooteco.chess.entity.Room;
import wooteco.chess.service.ChessService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RoomController.class)
class RoomControllerTest {

    @MockBean(ChessService.class)
    ChessService chessService;

    @Autowired
    MockMvc mvc;

    @Test
    void list() throws Exception {
        List<Room> rooms = new ArrayList<>();
        Room room = new Room("lowoon");
        rooms.add(room);

        given(chessService.getRooms()).willReturn(rooms);

        mvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("main"))
            .andExpect(model().attributeExists("rooms"))
            .andExpect(model().attribute("rooms", Collections.singletonList(room)));
    }
}