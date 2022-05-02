package chess.service;

import chess.domain.piece.PieceColor;
import chess.dto.request.CreateRoomDto;
import chess.dto.request.MovePieceDto;
import chess.dto.response.RoomDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChessServiceTest {
    public static final String TEST_ROOM_TITLE = "test-game-room";
    public static final String TEST_ROOM_PASSWORD = "1234";
    private final CreateRoomDto CREATE_ROOM_DTO = new CreateRoomDto(TEST_ROOM_TITLE, TEST_ROOM_PASSWORD);

    private ChessService chessService;
    private String createdTestRoomId;

    @BeforeEach
    void setUp() {
        chessService = new ChessService(new RoomDaoFake(), new BoardDaoFake());
        RoomDto roomDto = chessService.createRoom(CREATE_ROOM_DTO);
        createdTestRoomId = roomDto.getId();
    }

    @DisplayName("기물 이동")
    @Test
    void movePiece() {
        MovePieceDto movePieceDto = new MovePieceDto("a2", "a3");
        chessService.movePiece(createdTestRoomId, movePieceDto);
    }

    @DisplayName("현재 차례 색상 가져오기")
    @Test
    void getCurrentTurn() {
        // given & when
        PieceColor actual = chessService.getCurrentTurn(createdTestRoomId).getPieceColor();

        // then
        assertThat(actual).isEqualTo(PieceColor.WHITE);
    }

    @DisplayName("검정팀 점수 가져오기")
    @Test
    void getScore_black() {
        // given & when
        double actual = chessService.getScore(createdTestRoomId).getBlackScore();

        // then
        assertThat(actual).isEqualTo(38.0);
    }

    @DisplayName("흰팀 점수 가져오기")
    @Test
    void getScore_white() {
        // given & when
        double actual = chessService.getScore(createdTestRoomId).getWhiteScore();

        // then
        assertThat(actual).isEqualTo(38.0);
    }

    @DisplayName("승자팀 가져오기")
    @Test
    void getWinColor() {
        // given
        chessService.movePiece(createdTestRoomId, new MovePieceDto("b1", "c3"));
        chessService.movePiece(createdTestRoomId, new MovePieceDto("a7", "a6"));
        chessService.movePiece(createdTestRoomId, new MovePieceDto("c3", "b5"));
        chessService.movePiece(createdTestRoomId, new MovePieceDto("a6", "a5"));
        chessService.movePiece(createdTestRoomId, new MovePieceDto("b5", "c7"));
        chessService.movePiece(createdTestRoomId, new MovePieceDto("a5", "a4"));
        chessService.movePiece(createdTestRoomId, new MovePieceDto("c7", "e8"));

        // when
        PieceColor actual = chessService.getWinColor(createdTestRoomId).getPieceColor();

        // then
        assertThat(actual).isEqualTo(PieceColor.WHITE);
    }
}
