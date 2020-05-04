package wooteco.chess.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.dto.RowsDtoConverter;
import wooteco.chess.entity.GameEntity;
import wooteco.chess.entity.Room;
import wooteco.chess.service.ChessService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RoomController.class)
class RoomControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean(ChessService.class)
    ChessService chessService;

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

    @Test
    void start() throws Exception {
        GameEntity gameEntity = new GameEntity(0);
        Room room = new Room("lowoon",gameEntity);

        given(chessService.getTurn(room)).willReturn(0);
        given(chessService.getRowsDto(room)).willReturn(RowsDtoConverter.convertFrom(BoardFactory.createInitialBoard()));

        mvc.perform(get("/start/lowoon"))
            .andExpect(status().isOk())
            .andExpect(view().name("board"))
            .andExpect(model().attributeExists("room", "turn", "rows"))
            .andExpect(model().attribute("turn", 0))
            .andExpect(model().attribute("room", "lowoon"));
    }
}