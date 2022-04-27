package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.game.room.Room;
import chess.domain.game.room.RoomId;
import chess.domain.piece.PieceColor;
import chess.domain.position.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChessServiceTest {
    private static final String TEST_GAME_ROOM_ID = "test-game-id";
    private final Room TEST_GAME_ROOM = Room.from(TEST_GAME_ROOM_ID, "test-game-room", "1234");

    private ChessService chessService;

    @BeforeEach
    void setUp() {
        chessService = new ChessService(new GameRoomDaoFake(), new BoardDaoFake());
        chessService.createGame(TEST_GAME_ROOM);
    }

    @DisplayName("기물 이동")
    @Test
    void movePiece() {
        chessService.movePiece(RoomId.from(TEST_GAME_ROOM_ID), Position.from("a2"), Position.from("a3"));
    }

    @DisplayName("현재 차례 색상 가져오기")
    @Test
    void getCurrentTurn() {
        // given & when
        PieceColor actual = chessService.getCurrentTurn(RoomId.from(TEST_GAME_ROOM_ID));

        // then
        assertThat(actual).isEqualTo(PieceColor.WHITE);
    }

    @DisplayName("검정팀 점수 가져오기")
    @Test
    void getScore_black() {
        // given & when
        double actual = chessService.getScore(RoomId.from(TEST_GAME_ROOM_ID), PieceColor.BLACK).getValue();

        // then
        assertThat(actual).isEqualTo(38.0);
    }

    @DisplayName("흰팀 점수 가져오기")
    @Test
    void getScore_white() {
        // given & when
        double actual = chessService.getScore(RoomId.from(TEST_GAME_ROOM_ID), PieceColor.WHITE).getValue();

        // then
        assertThat(actual).isEqualTo(38.0);
    }

    @DisplayName("승자팀 가져오기")
    @Test
    void getWinColor() {
        // given
        chessService.movePiece(RoomId.from(TEST_GAME_ROOM_ID), Position.from("b1"), Position.from("c3"));
        chessService.movePiece(RoomId.from(TEST_GAME_ROOM_ID), Position.from("a7"), Position.from("a6"));
        chessService.movePiece(RoomId.from(TEST_GAME_ROOM_ID), Position.from("c3"), Position.from("b5"));
        chessService.movePiece(RoomId.from(TEST_GAME_ROOM_ID), Position.from("a6"), Position.from("a5"));
        chessService.movePiece(RoomId.from(TEST_GAME_ROOM_ID), Position.from("b5"), Position.from("c7"));
        chessService.movePiece(RoomId.from(TEST_GAME_ROOM_ID), Position.from("a5"), Position.from("a4"));
        chessService.movePiece(RoomId.from(TEST_GAME_ROOM_ID), Position.from("c7"), Position.from("e8"));

        // when
        PieceColor actual = chessService.getWinColor(RoomId.from(TEST_GAME_ROOM_ID));

        // then
        assertThat(actual).isEqualTo(PieceColor.WHITE);
    }
}
