package chess.service;

import chess.dao.PieceDao;
import chess.dao.RoomDao;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.player.Round;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.repository.ChessRepository;
import chess.repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
@Sql("classpath:schema.sql")
@JdbcTest
public class InMemoryChessServiceTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private ChessService chessService;

    @BeforeEach
    void setUp() {
        ChessRepository inMemoryRepository = new InMemoryRepository(
                new RoomDao(this.jdbcTemplate, this.namedParameterJdbcTemplate),
                new PieceDao(this.jdbcTemplate, this.namedParameterJdbcTemplate)
        );
        chessService = new ChessService(inMemoryRepository);
    }

    @DisplayName("방 생성 테스트")
    @Test
    void makeRoomTest() {
        assertEquals(4, chessService.makeRoom("room4"));
    }

    @DisplayName("방 id를 통해 게임을 가져오는 테스트")
    @Test
    void getStoredRoundTest() {
        chessService.makeRoom("room4");
        Round round = chessService.getStoredRound(4);
        assertEquals(81, round.getBoard().size());
        assertEquals("white", round.getCurrentTurn());
        assertEquals(38, round.getPlayerScore("white"));
        assertEquals(38, round.getPlayerScore("black"));
    }

    @DisplayName("기물의 움직임 테스트")
    @Test
    void movePieceTest() {
        chessService.makeRoom("room4");
        chessService.movePiece("e2", "e4", 4);
        Map<Position, Piece> board = chessService.getStoredRound(4).getBoard();
        assertNull(board.get(Position.valueOf("2", "e")));
        assertEquals("p", board.get(Position.valueOf("4", "e")).getPiece());
    }

    @DisplayName("차례 바꾸기 테스트")
    @Test
    void changeTurnTest() {
        chessService.makeRoom("room4");
        chessService.changeTurn("black", "white", 4);
        assertEquals("black", chessService.getStoredRound(4).getCurrentTurn());
    }

    @DisplayName("방 이름 목록 가져오기 테스트")
    @Test
    void getRoomNamesTest() {
        Map<Integer, String> roomNames = chessService.getRoomNames();
        assertEquals(3, roomNames.size());
        assertEquals("room1", roomNames.get(1));
    }

    @DisplayName("방 이름으로 id 가져오기 테스트")
    @Test
    void getRoomIdTest() {
        assertEquals(2, chessService.getRoomId("room2"));
    }




}
