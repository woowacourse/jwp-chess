package chess.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import chess.domain.board.Score;
import chess.domain.chessgame.ChessGame;
import chess.domain.chessgame.Room;
import chess.domain.piece.Color;
import chess.domain.position.Position;
import chess.repository.RoomRepository;
import chess.repository.RoomRepositoryImpl;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@JdbcTest
@Transactional
public class RoomServiceTest {

    private RoomService roomService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        roomService = new RoomService(new RoomRepositoryImpl(jdbcTemplate));
        jdbcTemplate.execute("DROP TABLE pieces IF EXISTS");
        jdbcTemplate.execute("DROP TABLE room IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE pieces(room_id bigint(20), piece_name char(1), position char(2))");
        jdbcTemplate.execute("CREATE TABLE room(id bigint(20), turn char(5), playing_flag boolean)");
    }

    @Test
    @DisplayName("체스 게임 방 목록을 구한다.")
    void getRoomsTest() {
        roomService.postRoom(1);
        roomService.postRoom(2);
        roomService.postRoom(3);
        roomService.postRoom(4);
        roomService.postRoom(5);

        assertEquals(Arrays.asList(1, 2, 3, 4, 5), roomService.getRooms());
    }

    @Test
    @DisplayName("체스 게임 방이 없는 경우, 방을 만들고 DB에 해당 방 정보와 기물 정보 저장하고 방 정보를 리턴한다.")
    void postPiecesTestIfChessGameRoomNotExist() {
        Room newRoom = roomService.postRoom(1);

        assertTrue(newRoom.isPlaying());
        assertFalse(newRoom.isBlackTurn());
        assertEquals(64, newRoom.pieces().size());
        assertEquals(Color.NONE, newRoom.winnerColor());
    }

    @Test
    @DisplayName("체스 게임 방이 이미 존재하는 경우, DB에 저장된 해당 방 정보와 기물 정보를 리턴한다.")
    void postPiecesTestIfChessGameRoomExist() {
        Room oldRoom = roomService.postRoom(1);
        Room newRoom = roomService.postRoom(1);

        assertEquals(oldRoom.isPlaying(), newRoom.isPlaying());
        assertEquals(oldRoom.getId(), newRoom.getId());
        assertEquals(oldRoom.winnerColor(), newRoom.winnerColor());
    }

    @Test
    @DisplayName("기물을 이동시키고 db에 저장된 기물 정보 데이터를 업데이트 한다.")
    void putPiecesTest() {
        roomService.postRoom(1);

        Position source = new Position("a2");
        Position target = new Position("a4");

        ChessGame chessGame = roomService.putPieces(1, source, target);

        assertEquals(".", chessGame.pieces().get(source).getName());
        assertEquals("p", chessGame.pieces().get(target).getName());
    }

    @Test
    @DisplayName("체스 게임 점수를 계산한다.")
    void getScoreTest() {
        roomService.postRoom(1);
        Score initScore = new Score(38);

        assertEquals(initScore, roomService.getScore(1, Color.BLACK));
        assertEquals(initScore, roomService.getScore(1, Color.WHITE));
    }

}
