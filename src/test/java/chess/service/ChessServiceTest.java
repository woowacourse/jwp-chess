package chess.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import chess.dao.PiecesDao;
import chess.domain.piece.Color;
import chess.dto.request.PiecesRequestDto;
import chess.dto.response.PieceResponseDto;
import chess.dto.response.PiecesResponseDto;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class ChessServiceTest {

    private ChessService chessService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessService = new ChessService(new PiecesDao(jdbcTemplate));
        jdbcTemplate.execute("DROP TABLE pieces IF EXISTS");
        jdbcTemplate.execute("DROP TABLE room IF EXISTS");
        jdbcTemplate.execute(
            "CREATE TABLE pieces(room_id bigint(20), piece_name char(1), position char(2))");
        jdbcTemplate
            .execute("CREATE TABLE room(id bigint(20), turn char(5), playing_flag boolean)");
    }

    @Test
    @DisplayName("체스 게임 방 목록을 구한다.")
    void getRoomsTest() {
        chessService.postPieces(new PiecesRequestDto(1));
        chessService.postPieces(new PiecesRequestDto(2));
        chessService.postPieces(new PiecesRequestDto(3));
        chessService.postPieces(new PiecesRequestDto(4));
        chessService.postPieces(new PiecesRequestDto(5));

        assertEquals(Arrays.asList(1, 2, 3, 4, 5), chessService.getRooms().getRoomIds());
    }

    @Test
    @DisplayName("체스 게임 방이 없는 경우, 방을 만들고 DB에 해당 방 정보와 기물 정보 저장하고 방 정보를 리턴한다.")
    void postPiecesTestIfChessGameRoomNotExist() {
        PiecesRequestDto piecesRequestDto = new PiecesRequestDto(2);
        PiecesResponseDto piecesResponseDto = chessService.postPieces(piecesRequestDto);

        assertTrue(piecesResponseDto.isPlaying());
        assertEquals(64, piecesResponseDto.getAlivePieces().size());
        assertEquals(Color.NONE, piecesResponseDto.getWinnerColor());
    }

    @Test
    @DisplayName("체스 게임 방이 이미 존재하는 경우, DB에 저장된 해당 방 정보와 기물 정보를 리턴한다.")
    void postPiecesTestIfChessGameRoomExist() {
        jdbcTemplate
            .update("INSERT INTO room (id, turn, playing_flag) VALUES (?, ?, ?)", 1, "BLACK", true);
        jdbcTemplate
            .update("INSERT INTO pieces (room_id, piece_name, position) VALUES (?, ?, ?)", 1, "p",
                "a2");

        PiecesRequestDto piecesRequestDto = new PiecesRequestDto(1);
        PiecesResponseDto piecesResponseDto = chessService.postPieces(piecesRequestDto);

        assertTrue(piecesResponseDto.isPlaying());
        assertEquals("p", piecesResponseDto.getAlivePieces().get(0).getName());
        assertEquals(Color.NONE, piecesResponseDto.getWinnerColor());
    }

    @Test
    @DisplayName("기물을 이동시키고 db에 저장된 기물 정보 데이터를 업데이트 한다.")
    void putPiecesTest() {
        PiecesRequestDto piecesRequestDto = new PiecesRequestDto(1);
        chessService.postPieces(piecesRequestDto);
        PiecesResponseDto piecesResponseDto = chessService
            .putPieces(new PiecesRequestDto(1, "a2", "a4"));

        for (PieceResponseDto pieceResponseDto : piecesResponseDto.getAlivePieces()) {
            if (pieceResponseDto.getPosition().equals("a4")) {
                assertEquals("p", pieceResponseDto.getName());
            }
            if (pieceResponseDto.getPosition().equals("a2")) {
                assertEquals(".", pieceResponseDto.getName());
            }
        }
    }

    @Test
    @DisplayName("체스 게임 점수를 계산한다.")
    void getScoreTest() {
        PiecesRequestDto piecesRequestDto = new PiecesRequestDto(1);
        chessService.postPieces(piecesRequestDto);

        assertEquals(38, chessService.getScore(1, "BLACK").getScore());
        assertEquals(38, chessService.getScore(1, "WHITE").getScore());
    }

}
