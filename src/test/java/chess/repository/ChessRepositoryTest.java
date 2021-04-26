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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.CollectionUtils;

import chess.dao.ChessRepository;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.position.Position;

@JdbcTest
@TestPropertySource("classpath:application.properties")
@Sql("classpath:initSetting.sql")
public class ChessRepositoryTest {

    private ChessRepository chessRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessRepository = new ChessRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("방의 모든 기물들을 가져온다. 기물이 없는 경우")
    void findOnePieceByRoomId() {
        int roomId = Integer.parseInt(chessRepository.findIdByTitle("hi"));

        assertEquals(0, chessRepository.findPiecesByRoomId(roomId).size());
    }

    @Test
    @DisplayName("방의 모든 기물들을 가져온다. 기물이 채워진 경우")
    void findAllPiecesByRoomId() {
        int roomId = Integer.parseInt(chessRepository.findIdByTitle("hi"));
        String pieceName = "r";
        String position = "a7";
        chessRepository.insertPieceByRoomId(roomId, pieceName, position);
        assertEquals(1, chessRepository.findPiecesByRoomId(roomId).size());
        assertEquals("r", chessRepository.findPiecesByRoomId(roomId).get(new Position("a7")).getName());
    }

    @Test
    @DisplayName("모든 방의 이름을 가져온다.")
    void findAllRoomId() {
        assertEquals(1, chessRepository.findAllRoomName().size());
        assertEquals("hi", chessRepository.findAllRoomName().get(0));
    }

    @Test
    @DisplayName("새로운 방을 추가한다.")
    void insertNewRoom() {
        assertDoesNotThrow(() -> chessRepository.insertRoom("hello"));
        assertEquals(2, chessRepository.findAllRoomName().size());
        assertEquals("hello", chessRepository.findAllRoomName().get(1));
    }

    @Test
    @DisplayName("방의 turn을 찾는다.")
    void findTurnByRoomId() {
        int roomId = Integer.parseInt(chessRepository.findIdByTitle("hi"));
        assertEquals("WHITE", chessRepository.findTurnByRoomId(roomId));
    }

    @Test
    @DisplayName("방이 게임이 끝났는지 찾는다.")
    void findPlayingFlagByRoomId() {
        int roomId = Integer.parseInt(chessRepository.findIdByTitle("hi"));
        assertTrue(chessRepository.findPlayingFlagByRoomId(roomId));
    }

    @Test
    @DisplayName("방의 정보를 수정한다.")
    void updateRoom() {
        int roomId = Integer.parseInt(chessRepository.findIdByTitle("hi"));
        assertDoesNotThrow(() -> chessRepository.updateRoom(roomId, false, false));
        assertFalse(chessRepository.findPlayingFlagByRoomId(roomId));
    }

    @Test
    @DisplayName("방의 기물 정보를 수정한다.")
    void updatePieces() {
        int roomId = Integer.parseInt(chessRepository.findIdByTitle("hi"));
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
        int roomId = Integer.parseInt(chessRepository.findIdByTitle("hi"));
        String pieceName = "k";
        String position = "a5";

        assertDoesNotThrow(() -> chessRepository.insertPieceByRoomId(roomId, pieceName, position));
        assertEquals(1, chessRepository.findPiecesByRoomId(roomId).size());
        assertEquals("k", chessRepository.findPiecesByRoomId(roomId).get(new Position("a5")).getName());
    }

    @Test
    @DisplayName("방의 기물 정보를 삭제한다.")
    void deletePieces() {
        int roomId = Integer.parseInt(chessRepository.findIdByTitle("hi"));

        assertDoesNotThrow(() -> chessRepository.deleteAllPiecesByRoomId(roomId));
        assertTrue(CollectionUtils.isEmpty(chessRepository.findPiecesByRoomId(roomId)));
    }

    @Test
    @DisplayName("방을 삭제한다.")
    void deleteRoom() {
        int roomId = Integer.parseInt(chessRepository.findIdByTitle("hi"));

        assertDoesNotThrow(() -> chessRepository.deleteRoomById(roomId));
        assertTrue(CollectionUtils.isEmpty(chessRepository.findAllRoomName()));
    }

    @Test
    @DisplayName("방의 id를 가져온다.")
    void findId() {
        String roomId = chessRepository.findIdByTitle("hi");

        assertEquals("1", roomId);
    }
}
