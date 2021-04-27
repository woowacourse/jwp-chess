package chess.controller.spring;

import chess.controller.spring.vo.SessionVO;
import chess.domain.board.ChessBoard;
import chess.domain.piece.TeamType;
import chess.dto.MoveRequestDTO;
import chess.dto.board.BoardDTO;
import chess.service.spring.ChessService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext
@ActiveProfiles("test")
class ChessControllerTest {

    private int roomId;

    @Autowired
    private ChessService chessService;

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
        roomId = roomService.addRoom("room1", "pass1");
        userService.addUserIntoRoom(roomId, "anotheruser123");
    }

    @AfterEach
    void tearDown() {
        chessService.deleteGame(roomId, roomId);
    }

    @DisplayName("보드를 조회한다.")
    @Test
    void showBoard() throws Exception {
        String expectedResponseBody = writeResponseBody();

        mockMvc.perform(get("/chessgame/" + roomId + "/chessboard")
                .sessionAttr("session", new SessionVO(roomId, "pass1"))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponseBody));
    }

    private String writeResponseBody() throws JsonProcessingException {
        ChessBoard chessBoard = chessService.findChessBoard(roomId);
        TeamType currentTeamType = chessService.findCurrentTeamType(roomId);
        BoardDTO boardDTO = BoardDTO.of(chessBoard, currentTeamType);
        return new ObjectMapper().writeValueAsString(boardDTO);
    }

    private String writeRequestBody(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    @DisplayName("체스 기물을 이동한다.")
    @Test
    void move() throws Exception {
        MoveRequestDTO moveRequestDTO = new MoveRequestDTO("a2", "a3", "WHITE");
        String requestBody = writeRequestBody(moveRequestDTO);

        mockMvc.perform(put("/chessgame/" + roomId + "/chessboard")
                .sessionAttr("session", new SessionVO(roomId, "pass1"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(writeResponseBody()));
    }

    @DisplayName("현재 턴이 아닌 기물을 조작시 예외가 발생한다.")
    @Test
    void cannotMove() throws Exception {
        MoveRequestDTO moveRequestDTO = new MoveRequestDTO("a2", "a3", "BLACK");
        String requestBody = writeRequestBody(moveRequestDTO);

        mockMvc.perform(put("/chessgame/" + roomId + "/chessboard")
                .sessionAttr("session", new SessionVO(roomId, "pass1"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("조작할 수 있는 기물이 없습니다."));
    }

    @DisplayName("게임 종료시 응답 메시지는 메인 페이지 url이다.")
    @Test
    void exit() throws Exception {
        int newRoomId = roomService.addRoom("room2", "pass369");

        mockMvc.perform(delete("/chessgame/" + newRoomId)
                .sessionAttr("session", new SessionVO(newRoomId, "pass369"))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string("/"));
    }
}
