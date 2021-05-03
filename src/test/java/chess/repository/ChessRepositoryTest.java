package chess.repository;

import chess.dao.PieceDao;
import chess.dao.RoomDao;
import chess.domain.piece.Piece;
import chess.domain.player.Round;
import chess.domain.position.Position;
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

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
@Sql("classpath:schema.sql")
@JdbcTest
public class ChessRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private ChessRepository chessRepository;


    @BeforeEach
    void setUp() {
        this.chessRepository = new ChessRepository(
                new RoomDao(this.jdbcTemplate, this.namedParameterJdbcTemplate), new PieceDao(this.jdbcTemplate, this.namedParameterJdbcTemplate)
        );
    }

    private Map<String, String> filteredChessBoard(final Map<Position, Piece> chessBoard) {
        Map<String, String> filteredChessBoard = new LinkedHashMap<>();
        for (Map.Entry<Position, Piece> cell : chessBoard.entrySet()) {
            filter(filteredChessBoard, cell);
        }
        return filteredChessBoard;
    }

    private void filter(Map<String, String> filteredChessBoard, Map.Entry<Position, Piece> cell) {
        if (isPieceExist(cell)) {
            filteredChessBoard.put(cell.getKey().toString(), cell.getValue().getPiece());
        }
    }

    private boolean isPieceExist(Map.Entry<Position, Piece> chessBoardEntry) {
        return chessBoardEntry.getValue() != null;
    }

    @DisplayName("방 생성 테스트")
    @Test
    void makeRoomTest() {
        Round round = new Round();
        Map<Position, Piece> board = round.getBoard();
        int roomId = chessRepository.makeRoom(filteredChessBoard(board), "room4");
        assertEquals(4, roomId);
    }

    @DisplayName("기물 초기화 테스트")
    @Test
    void initializePieceStatusTest() {
        Round round = new Round();
        Map<Position, Piece> board = round.getBoard();
        int roomId = chessRepository.makeRoom(filteredChessBoard(board), "room4");
        chessRepository.initializePieceStatus(roomId, filteredChessBoard(board));
    }

    @DisplayName("roomId로 보드 받아오는 테스트")
    @Test
    void getBoardByRoomIdTest() {
        Round round = new Round();
        Map<Position, Piece> board = round.getBoard();
        int roomId = chessRepository.makeRoom(filteredChessBoard(board), "room4");
        chessRepository.initializePieceStatus(roomId, filteredChessBoard(board));
        Map<Position, Piece> boardByRoomId = chessRepository.getBoardByRoomId(roomId);
        assertEquals(32, boardByRoomId.size());
    }

    @DisplayName("차례 바꾸기 테스트")
    @Test
    void changeTurnTest() {
        assertDoesNotThrow(
                () -> chessRepository.changeTurn("black", "white", 3)
        );
    }

    @DisplayName("roomId로 차례 확인 테스트")
    @Test
    void getCurrentTurnByRoomIdTest() {
        assertEquals("white", chessRepository.getCurrentTurnByRoomId(3));
    }

    @DisplayName("방 제목 목록 테스트")
    @Test
    void getRoomNamesTest() {
        assertEquals(3, chessRepository.getRoomNames().size());
    }

    @DisplayName("room Id 가져오는 테스트")
    @Test
    void getRoomId() {
        assertEquals(2, chessRepository.getRoomId("room2"));
    }
}
