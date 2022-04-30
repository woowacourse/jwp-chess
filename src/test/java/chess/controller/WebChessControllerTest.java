package chess.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chess.config.MockMvcConfig;
import chess.domain.board.BoardFactory;
import chess.domain.game.Score;
import chess.dto.BoardsDto;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.RoomRequestDto;
import chess.dto.response.ErrorResponseDto;
import chess.dto.response.ErrorResponseDto.StatusResponseDto;
import chess.dto.response.GameResponseDto;
import chess.dto.response.RoomResponseDto;
import chess.dto.response.RoomsResponseDto;
import chess.entity.BoardEntity;
import chess.entity.RoomEntity;
import chess.exception.NotFoundException;
import chess.service.ChessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@Import(MockMvcConfig.class)
@ActiveProfiles("test")
@WebMvcTest(WebChessController.class)
class WebChessControllerTest {

    private final String REQUEST_MAPPING_URI = "/api/chess/rooms";
    private final RoomEntity ROOM_A = new RoomEntity(1L, "체스 초보만", "white", false);
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChessService chessService;


    @DisplayName("방 생성 요청이 성공하면, 201 status 및 header 속 Location을 응답한다.")
    @Test
    void createRoom_success() throws Exception {
        final Long firstRoomId = 1L;

        //2. given
        // 2-1. post시 requestDto + objectMapper로 StringJson requestBody 만들기
        final RoomRequestDto roomRequestDto = new RoomRequestDto("체스 초보만");
        final String requestBody = objectMapper.writeValueAsString(roomRequestDto);

        // 2-2. controller에서 응답되어야하는 데이터가 존재 한다면, <그부분만> 가짜service의 return으로 만들어주기( 여기선 created후 생성된 데이터의 id -> Location에 확인)
        // -> BDDMockito: 메서드호출 + 인자는 암거나 any()로 가능 -> willReturn로 만들어주면
        // --> 최종 응답까지 기존 controller 움직임대도 움직이게 된다.
        given(chessService.createRoom(any())).willReturn(
            RoomResponseDto.of(new RoomEntity(firstRoomId, "체스 초보만", "white", false)));

        //1. when/then
        mockMvc.perform(post(REQUEST_MAPPING_URI).content(requestBody) // POST 요청 필수1. requestBody
                .contentType(MediaType.APPLICATION_JSON)) // POST 요청필수2. contentType
            .andExpect(status().isCreated()) // status 확인
            .andExpect(header().string(HttpHeaders.LOCATION, REQUEST_MAPPING_URI + "/" + firstRoomId));
    }

    @DisplayName("진행 중인 모든 방을 조회 요청이 성공하면, 200 status 및 방 정보들 데이터를 반환한다")
    @Test
    void findRooms_success() throws Exception {
        //2. given
        //2-1. response Body 예상값을 responseDto + objectMapper로 만든다.
        final RoomEntity roomA = new RoomEntity(1L, "체스 초보만", "white", false);
        final RoomEntity roomB = new RoomEntity(2L, "체스 초보만", "white", false);
        final RoomsResponseDto roomsResponseDto = RoomsResponseDto.of(List.of(roomA, roomB));
        final String responseBody = objectMapper.writeValueAsString(roomsResponseDto);

        //2-2. controller에서 반환될 데이터가 있다면,  BDDMockito + @MockBean-짭service로 만들어주면, 알아서 controller로직을 수행하여 응답된다
        //     1) controller에서 반환될 데이터는 원래 형으로 반환시켜주면 됨
        //     2) 원래형의 데이터 -> objectMapper -> responseBody -> mockMVc에서는 content()로 받아서 .string( )으로 바꾼것과 비교
        given(chessService.findRooms()).willReturn(roomsResponseDto);

        //1. when/then
        mockMvc.perform(get(REQUEST_MAPPING_URI)).andExpect(status().isOk()) // 응답 필수1. status 확인
            .andExpect(content().string(responseBody));
    }

    @DisplayName("방 입장 요청이 성공하면, 200 status 및 해당 방의 체스게임 데이터를 반환한다.")
    @Test
    void enterRoom_success() throws Exception {
        final Long firstRoomId = 1L;

        // 2. 조회시 받을 responseDto는 id가 배정된  Entity로부터 만든 뒤 -> objMapper로 변환
        final GameResponseDto gameResponseDto = GameResponseDto.of(ROOM_A, BoardsDto.of(getBoardInRoom(1L)));

        final String responseBody = objectMapper.writeValueAsString(gameResponseDto);

        // 3. controller가 해당 데이터를 응답해주도록 짭service로 willReturn(만든데이터)로 대체해주기
        given(chessService.getCurrentBoards(any()))
            .willReturn(gameResponseDto);

        // 1. when/then
        mockMvc.perform(get(REQUEST_MAPPING_URI + "/" + firstRoomId))
            .andExpect(status().isOk())
            .andExpect(content().string(responseBody));
    }

    @DisplayName("방 입장 요청이 실패하면, 404 status(NotFoundException) 및 예외 메세지를 응답한다.")
    @Test
    void enterRoom_fail() throws Exception {
        //2.
        final Long invalidRoomId = -1L;

        // responseDto 중에 Error는 Entity가 없이 바로 생성한다.
        final ErrorResponseDto errorResponseDto = new ErrorResponseDto("[ERROR] 방 정보를 찾을 수 없습니다.");
        final String responseBody = objectMapper.writeValueAsString(errorResponseDto);

        // controller에서는 정상요청만 응답하고,
        // service내부에서 throw를 발생시킨 뒤 -> RestControllerAdvice - WebChessControllerAdvice에 걸리게 했었다.
        // -> controller입장에서는 해당 service 호출시 에러가 발생하게 하면 된다.
        given(chessService.getCurrentBoards(invalidRoomId))
            .willThrow(new NotFoundException(1));

        //1. when/then
        mockMvc.perform(get(REQUEST_MAPPING_URI + "/" + invalidRoomId))
            .andExpect(status().isNotFound()) // 1. status는 예외발생처럼 나오지만
            .andExpect(content().string(responseBody)); // 2. errorResponseDto도 내려간다(view에 에러메세지 전달)
    }

    @DisplayName("기물 이동 요청이 성공하면, 200 status 및 방의 체스게임 데이터를 반환한다.")
    @Test
    void movePiece_success() throws Exception {
        final Long roomId = 1L;
        // 2. given : update(PATCH)는 request / response 둘다 데이터가 필요하다.
        // 2-1) request는 dto + ObjectMapper -> StringJson으로 만들어서 .content()안에 넣어준다.
        final MoveRequestDto moveRequestDto = new MoveRequestDto("a2", "a4");
        final String requestBody = objectMapper.writeValueAsString(moveRequestDto);
        // 2-2) response는 (entity ->) dto + objectMapper -> StringJson 으로 만들어서 .content().string(   )안에 넣어 비교
        // 2-3) response는  controller내 로직이 짭service로 흘러가도록
        //      -> given() .willReturn (or .willThrow)를 해준다.
        final GameResponseDto gameResponseDto = GameResponseDto.of(ROOM_A, BoardsDto.of(getBoardInRoom(1L)));
        final String responseBody = objectMapper.writeValueAsString(gameResponseDto);

        given(chessService.move(anyLong(), any(MoveRequestDto.class)))
            .willReturn(gameResponseDto);

        //1. when/then
        mockMvc.perform(patch(REQUEST_MAPPING_URI + "/" + roomId + "/move")
                .content(requestBody) // requestDto + objMapper
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(responseBody));
    }

    @DisplayName("기물 이동 요청이 실패하면, 400 status 및 예외 메세지를 응답한다.")
    @Test
    void movePiece_fail() throws Exception {
        //2. given: 예외가 발생한다면,
        //2-1. requestBody는 그대로이나
        //2-2. responseDto + objMapper로 responseBody + .willReturn() 이 아니라
        //     errorResponseDto + objMapper로 responseBody + .willThrow()로 더 간편해진다.
        final Long roomId = 1L;
        final MoveRequestDto moveRequestDto = new MoveRequestDto("a2", "e7");
        final String requestBody = objectMapper.writeValueAsString(moveRequestDto);

        final ErrorResponseDto errorResponseDto = new ErrorResponseDto("[ERROR] 이동할 수 없습니다.");
        final String responseBody = objectMapper.writeValueAsString(errorResponseDto);

        given(chessService.move(anyLong(), any(MoveRequestDto.class)))
            .willThrow(new IllegalStateException("[ERROR] 이동할 수 없습니다."));

        //1. when/then
        mockMvc.perform(patch(REQUEST_MAPPING_URI + "/" + roomId + "/move")
                .content(requestBody) // requestDto + objMapper
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(responseBody));
    }


    @DisplayName("게임 종료 요청이 성공하면, 200 status를 응답한다.")
    @Test
    void finishGame_success() throws Exception {
        final Long roomId = 1L;

        // doNothing().when( ): 응답이 없는 service메서드는 아래와 같이 시킨다.
        doNothing().when(chessService)
            .endRoom(anyLong());

        mockMvc.perform(patch(REQUEST_MAPPING_URI + "/" + roomId))
            .andExpect(status().isOk());
    }

    @DisplayName("게임 종료한 상태에서 재요청하면, 400 status와 예외 메세지를 응답한다.")
    @Test
    void finishGame_fail() throws Exception {
        final Long roomId = 1L;
        // response 1. body
        final ErrorResponseDto errorResponseDto = new ErrorResponseDto("[ERROR] 이미 종료된 게임입니다.");
        final String responseBody = objectMapper.writeValueAsString(errorResponseDto);
        // reponse 2. controller 짭 service에서 예외발생시키기

        // doThrow().when( ):응답이 없는 service메서드에서 예외를 발생시킬 때는 아래와 같이시킨다.
        doThrow(new IllegalStateException("[ERROR] 이미 종료된 게임입니다.")).when(chessService)
            .endRoom(anyLong());

        mockMvc.perform(patch(REQUEST_MAPPING_URI + "/" + roomId))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(responseBody));
    }

    @DisplayName("점수 계산 요청이 성공하면, 200 status 및 점수 데이터를 응답한다.")
    @Test
    void calculateStatus() throws Exception {
        final Long roomID = 1L;

        final StatusResponseDto statusResponseDto = StatusResponseDto.of(new Score(BoardFactory.initialize()));
        final String responseBody = objectMapper.writeValueAsString(statusResponseDto);

        given(chessService.calculateStatus(anyLong()))
            .willReturn(statusResponseDto);

        //1.when/then
        mockMvc.perform(get(REQUEST_MAPPING_URI + "/" + roomID + "/status"))
            .andExpect(status().isOk())
            .andExpect(content().string(responseBody));
    }

    private List<BoardEntity> getBoardInRoom(final Long roomId) {
        return BoardFactory.initialize()
            .entrySet()
            .stream()
            .map(entry -> new BoardEntity(
                roomId,
                entry.getKey().convertPositionToString(),
                entry.getValue().convertPieceToString())
            )
            .collect(Collectors.toList());
    }
}
