package chess.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.CollectionUtils;

import chess.dao.ChessRepository;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.position.Position;

@JdbcTest
@TestPropertySource("classpath:application-test.properties")
public class ChessRepositoryTest {

    private ChessRepository chessRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessRepository = new ChessRepository(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE pieces IF EXISTS");
        jdbcTemplate.execute("DROP TABLE room IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE pieces(room_id bigint, piece_name char(1), position char(2))");
        jdbcTemplate.execute("CREATE TABLE room(id bigint, turn char(5), playing_flag boolean)");
        jdbcTemplate.execute("INSERT INTO room (id, turn, playing_flag) values (1, 'WHITE', true)");
        jdbcTemplate.execute("INSERT INTO pieces (room_id, piece_name, position) values (1, 'p', 'a8')");
    }

    @Test
    @DisplayName("방의 모든 기물들을 가져온다.")
    void findAllPiecesByRoomId() {
        int roomId = 1;

        assertEquals(1, chessRepository.findPiecesByRoomId(roomId).size());
        assertEquals("p", chessRepository.findPiecesByRoomId(roomId).get(new Position("a8")).getName());
    }

    @Test
    @DisplayName("모든 방의 번호를 가져온다.")
    void findAllRoomId() {
        assertEquals(1, chessRepository.findAllRoomId().size());
        assertEquals(1, chessRepository.findAllRoomId().get(0));
    }

    @Test
    @DisplayName("새로운 방을 추가한다.")
    void insertNewRoom() {
        assertDoesNotThrow(() -> chessRepository.insertRoom(2));
        assertEquals(2, chessRepository.findAllRoomId().size());
        assertEquals(2, chessRepository.findAllRoomId().get(1));
    }

    @Test
    @DisplayName("방의 turn을 찾는다.")
    void findTurnByRoomId() {
        int roomId = 1;
        assertEquals("WHITE", chessRepository.findTurnByRoomId(roomId));
    }

    @Test
    @DisplayName("방이 게임이 끝났는지 찾는다.")
    void findPlayingFlagByRoomId() {
        int roomId = 1;
        assertTrue(chessRepository.findPlayingFlagByRoomId(roomId));
    }

    @Test
    @DisplayName("방의 정보를 수정한다.")
    void updateRoom() {
        int roomId = 1;
        assertDoesNotThrow(() -> chessRepository.updateRoom(1, false, false));
        assertFalse(chessRepository.findPlayingFlagByRoomId(roomId));
    }

    @Test
    @DisplayName("방의 기물 정보를 수정한다.")
    void updatePieces() {
        int roomId = 1;
        String pieceName = "r";
        String position = "a7";

        Map<Position, Piece> board = new HashMap<>();
        board.put(new Position(position), PieceFactory.of(pieceName));
        assertDoesNotThrow(() -> chessRepository.updatePiecesByRoomId(roomId, board));
        assertEquals(1, chessRepository.findPiecesByRoomId(roomId).size());
        assertEquals("r", chessRepository.findPiecesByRoomId(roomId).get(new Position("a7")).getName());
    }

    @Test
    @DisplayName("방의 기물을 추가한다.")
    void insertPiece() {
        int roomId = 1;
        String pieceName = "k";
        String position = "a5";

        assertDoesNotThrow(() -> chessRepository.insertPieceByRoomId(roomId, pieceName, position));
        assertEquals(2, chessRepository.findPiecesByRoomId(roomId).size());
        assertEquals("p", chessRepository.findPiecesByRoomId(roomId).get(new Position("a8")).getName());
        assertEquals("k", chessRepository.findPiecesByRoomId(roomId).get(new Position("a5")).getName());
    }

    @Test
    @DisplayName("방의 기물 정보를 삭제한다.")
    void deletePieces() {
        int roomId = 1;

        assertDoesNotThrow(() -> chessRepository.deleteAllPiecesByRoomId(roomId));
        assertTrue(CollectionUtils.isEmpty(chessRepository.findPiecesByRoomId(roomId)));
    }

    @Test
    @DisplayName("방을 삭제한다.")
    void deleteRoom() {
        int roomId = 1;

        assertDoesNotThrow(() -> chessRepository.deleteRoomById(roomId));
        assertTrue(CollectionUtils.isEmpty(chessRepository.findAllRoomId()));
    }
}
