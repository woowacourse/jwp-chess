package chess.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import chess.dao.ChessRepository;
import chess.domain.chessgame.ChessGame;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.response.PiecesResponseDto;

@JdbcTest
@TestPropertySource("classpath:application.properties")
@Sql("classpath:initSetting.sql")
public class ChessServiceTest {

    private ChessService chessService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessService = new ChessService(new ChessRepository(jdbcTemplate));
    }

    @Test
    @DisplayName("체스 게임 방이 없는 경우, 방을 만들고 DB에 해당 방의 정보와 기물 정보를 저장한다.")
    void addRoomTest1() {
        PiecesResponseDto piecesResponseDto = new PiecesResponseDto(chessService.postPieces(5));

        assertTrue(piecesResponseDto.isPlaying());
        assertEquals(64, piecesResponseDto.getPiecesInBoard().size());
        assertEquals(Color.NONE, piecesResponseDto.getWinnerColor());
    }

    @Test
    @DisplayName("체스 게임 방이 이미 존재하는 경우, 해당 방 정보와 기물 정보를 가져온다.")
    void addRoomTest2() {
        jdbcTemplate.update("INSERT INTO room (id, title, turn, playing_flag) VALUES (?, ?, ?, ?)", 1, "hello", "BLACK",
            true);
        jdbcTemplate.update("INSERT INTO pieces (room_id, piece_name, position) VALUES (?, ?, ?)", 1, "p", "a2");

        PiecesResponseDto piecesResponseDto = new PiecesResponseDto(chessService.postPieces(1));

        assertTrue(piecesResponseDto.isPlaying());
        assertEquals("p", piecesResponseDto.getPiecesInBoard().get(0).getName());
        assertEquals(Color.NONE, piecesResponseDto.getWinnerColor());
    }

    @Test
    @DisplayName("기물을 이동시키고 기물 정보 데이터를 업데이트 한다.")
    void putBoardTest() {
        int roomId =chessService.postRooms("3");
        chessService.postPieces(roomId);
        ChessGame chessGame = chessService.putBoard(roomId, new Position("a2"), new Position("a4"));
        for (Map.Entry<Position, Piece> piece : chessGame.pieces().entrySet()) {
            if (piece.getKey().chessCoordinate().equals("a4")) {
                assertEquals("p", piece.getValue().getName());
            }
            if (piece.getKey().chessCoordinate().equals("a2")) {
                assertEquals(".", piece.getValue().getName());
            }
        }
    }

    @Test
    @DisplayName("체스 게임 점수를 계산한다.")
    void getScoreTest() {
        int roomId =chessService.postRooms("test");
        chessService.postPieces(roomId);

        assertEquals(38, chessService.getScore(roomId, "BLACK").getValue());
        assertEquals(38, chessService.getScore(roomId, "WHITE").getValue());
    }

    @Test
    @DisplayName("체스 게임 방 목록을 구한다.")
    void getRoomsTest() {
        chessService.postRooms("hi");
        chessService.postRooms("hello");
        chessService.postRooms("i");
        chessService.postRooms("am");
        chessService.postRooms("sally");

        assertEquals(Arrays.asList("hi", "hello", "i", "am", "sally"), chessService.getRooms());
    }

}
