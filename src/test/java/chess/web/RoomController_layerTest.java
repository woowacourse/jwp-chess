package chess.web;

import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.piece.PieceFactory;
import chess.domain.room.Room;
import chess.service.RoomService;
import chess.web.dto.RoomDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoomController.class)
class RoomController_layerTest {

    @MockBean
    private RoomService roomService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createNewRoom() throws Exception {
        when(roomService.createNewRoom("testRoom"))
                .thenReturn(
                        new RoomDto(
                                new Room(
                                        1L,
                                        "test",
                                        new ChessGame(1L, new Board(PieceFactory.createPieces()))
                                )
                        )
                );

        mockMvc.perform(post("/rooms/1"))
                .andExpect(status().isOk());
    }

    @Test
    void rooms() throws Exception {
        ChessGame dummyGame = new ChessGame(new Board(PieceFactory.createPieces()));
        when(roomService.getAllRooms()).thenReturn(Arrays.asList(
                new RoomDto(new Room("dummy", dummyGame)),
                new RoomDto(new Room("dummy", dummyGame)),
                new RoomDto(new Room("dummy", dummyGame))
        ));

        mockMvc.perform(get("/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(3));
    }
}