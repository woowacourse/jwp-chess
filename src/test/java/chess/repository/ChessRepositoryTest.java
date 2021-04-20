package chess.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.CollectionUtils;

import chess.dao.ChessRepository;
import chess.dto.PieceDto;
import chess.dto.PiecesDto;
import chess.dto.RoomIdDto;

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
        RoomIdDto roomIdDto = new RoomIdDto(1);

        assertEquals(1, chessRepository.findPiecesByRoomId(roomIdDto).size());
        assertEquals("p", chessRepository.findPiecesByRoomId(roomIdDto).get(0).getPieceName());
        assertEquals("a8", chessRepository.findPiecesByRoomId(roomIdDto).get(0).getPosition());
    }

    @Test
    @DisplayName("모든 방의 번호를 가져온다.")
    void findAllRoomId() {
        assertEquals(1, chessRepository.findAllRoomId().size());
        assertEquals(1, chessRepository.findAllRoomId().get(0).getId());
    }

    @Test
    @DisplayName("새로운 방을 추가한다.")
    void insertNewRoom() {
        RoomIdDto roomIdDto = new RoomIdDto(2);

        assertDoesNotThrow(() -> chessRepository.insertRoom(roomIdDto));
        assertEquals(2, chessRepository.findAllRoomId().size());
        assertEquals(2, chessRepository.findAllRoomId().get(1).getId());
    }

    @Test
    @DisplayName("방의 turn을 찾는다.")
    void findTurnByRoomId() {
        RoomIdDto roomIdDto = new RoomIdDto(1);

        assertEquals("WHITE", chessRepository.findTurnByRoomId(roomIdDto));
    }

    @Test
    @DisplayName("방이 게임이 끝났는지 찾는다.")
    void findPlayingFlagByRoomId() {
        RoomIdDto roomIdDto = new RoomIdDto(1);

        assertTrue(chessRepository.findPlayingFlagByRoomId(roomIdDto));
    }

    @Test
    @DisplayName("방의 정보를 수정한다.")
    void updateRoom() {
        RoomIdDto roomIdDto = new RoomIdDto(1);
        assertDoesNotThrow(() -> chessRepository.updateRoom(1, false, false));
        assertFalse(chessRepository.findPlayingFlagByRoomId(roomIdDto));
    }

    @Test
    @DisplayName("방의 기물 정보를 수정한다.")
    void updatePieces() {
        RoomIdDto roomIdDto = new RoomIdDto(1);
        PieceDto pieceDto = new PieceDto(1, "r", "a7");
        PiecesDto piecesDto = new PiecesDto(Collections.singletonList(pieceDto));

        assertDoesNotThrow(() -> chessRepository.updatePiecesByRoomId(piecesDto));
        assertEquals(2, chessRepository.findPiecesByRoomId(roomIdDto).size());
        assertEquals("p", chessRepository.findPiecesByRoomId(roomIdDto).get(0).getPieceName());
        assertEquals("a8", chessRepository.findPiecesByRoomId(roomIdDto).get(0).getPosition());
        assertEquals("r", chessRepository.findPiecesByRoomId(roomIdDto).get(1).getPieceName());
        assertEquals("a7", chessRepository.findPiecesByRoomId(roomIdDto).get(1).getPosition());
    }

    @Test
    @DisplayName("방의 기물을 추가한다.")
    void insertPiece() {
        PieceDto pieceDto = new PieceDto(1, "k", "a5");
        RoomIdDto roomIdDto = new RoomIdDto(1);

        assertDoesNotThrow(() -> chessRepository.insertPieceByRoomId(pieceDto));
        assertEquals(2, chessRepository.findPiecesByRoomId(roomIdDto).size());
        assertEquals("p", chessRepository.findPiecesByRoomId(roomIdDto).get(0).getPieceName());
        assertEquals("a8", chessRepository.findPiecesByRoomId(roomIdDto).get(0).getPosition());
        assertEquals("k", chessRepository.findPiecesByRoomId(roomIdDto).get(1).getPieceName());
        assertEquals("a5", chessRepository.findPiecesByRoomId(roomIdDto).get(1).getPosition());
    }

    @Test
    @DisplayName("방의 기물 정보를 삭제한다.")
    void deletePieces() {
        RoomIdDto roomIdDto = new RoomIdDto(1);

        assertDoesNotThrow(() -> chessRepository.deleteAllPiecesByRoomId(roomIdDto));
        assertTrue(CollectionUtils.isEmpty(chessRepository.findPiecesByRoomId(roomIdDto)));
    }

    @Test
    @DisplayName("방을 삭제한다.")
    void deleteRoom() {
        RoomIdDto roomIdDto = new RoomIdDto(1);

        assertDoesNotThrow(() -> chessRepository.deleteRoomById(roomIdDto));
        assertTrue(CollectionUtils.isEmpty(chessRepository.findAllRoomId()));
    }
}
