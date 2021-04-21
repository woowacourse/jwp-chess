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

import chess.dao.ChessRepository;
import chess.domain.chessgame.ChessGame;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.response.PiecesResponseDto;

@JdbcTest
@TestPropertySource("classpath:application-test.properties")
public class ChessServiceTest {

    private ChessService chessService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessService = new ChessService(new ChessRepository(jdbcTemplate));
        jdbcTemplate.execute("DROP TABLE pieces IF EXISTS");
        jdbcTemplate.execute("DROP TABLE room IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE pieces(room_id bigint(20), piece_name char(1), position char(2))");
        jdbcTemplate.execute("CREATE TABLE room(id bigint(20), turn char(5), playing_flag boolean)");
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
        jdbcTemplate.update("INSERT INTO room (id, turn, playing_flag) VALUES (?, ?, ?)", 1, "BLACK", true);
        jdbcTemplate.update("INSERT INTO pieces (room_id, piece_name, position) VALUES (?, ?, ?)", 1, "p", "a2");

        PiecesResponseDto piecesResponseDto = new PiecesResponseDto(chessService.postPieces(1));

        assertTrue(piecesResponseDto.isPlaying());
        assertEquals("p", piecesResponseDto.getPiecesInBoard().get(0).getName());
        assertEquals(Color.NONE, piecesResponseDto.getWinnerColor());
    }

    @Test
    @DisplayName("기물을 이동시키고 기물 정보 데이터를 업데이트 한다.")
    void putBoardTest() {
        chessService.postPieces(1);
        ChessGame chessGame = chessService.putBoard(1, new Position("a2"), new Position("a4"));
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
        chessService.postPieces(1);

        assertEquals(38, chessService.getScore(1, "BLACK").getValue());
        assertEquals(38, chessService.getScore(1, "WHITE").getValue());
    }

    @Test
    @DisplayName("체스 게임 방 목록을 구한다.")
    void getRoomsTest() {
        chessService.postPieces(1);
        chessService.postPieces(2);
        chessService.postPieces(3);
        chessService.postPieces(4);
        chessService.postPieces(5);

        assertEquals(Arrays.asList(1, 2, 3, 4, 5), chessService.getRooms());
    }

}
