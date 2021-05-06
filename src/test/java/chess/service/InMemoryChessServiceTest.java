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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InMemoryChessServiceTest {
    private ChessService chessService;

    @BeforeEach
    void setUp() {
        ChessRepository inMemoryRepository = new InMemoryRepository();
        chessService = new ChessService(inMemoryRepository);
    }

    @DisplayName("방 생성 테스트")
    @Test
    void makeRoomTest() {
        assertEquals(1, chessService.makeRoom("room1"));
    }

    @DisplayName("방 id를 통해 게임을 가져오는 테스트")
    @Test
    void getStoredRoundTest() {
        chessService.makeRoom("room1");
        Round round = chessService.getStoredRound(1);
        assertEquals(81, round.getBoard().size());
        assertEquals("white", round.getCurrentTurn());
        assertEquals(38, round.getPlayerScore("white"));
        assertEquals(38, round.getPlayerScore("black"));
    }

    @DisplayName("기물의 움직임 테스트")
    @Test
    void movePieceTest() {
        chessService.makeRoom("room1");
        chessService.movePiece("e2", "e4", 1);
        Map<Position, Piece> board = chessService.getStoredRound(1).getBoard();
        assertNull(board.get(Position.valueOf("2", "e")));
        assertEquals("p", board.get(Position.valueOf("4", "e")).getPiece());
    }

    @DisplayName("차례 바꾸기 테스트")
    @Test
    void changeTurnTest() {
        chessService.makeRoom("room1");
        chessService.changeTurn("black", "white", 1);
        assertEquals("black", chessService.getStoredRound(1).getCurrentTurn());
    }

    @DisplayName("방 이름 목록 가져오기 테스트")
    @Test
    void getRoomNamesTest() {
        chessService.makeRoom("room1");
        chessService.makeRoom("room2");
        chessService.makeRoom("room3");
        Map<Integer, String> roomNames = chessService.getRoomNames();
        assertEquals(3, roomNames.size());
        assertEquals("room1", roomNames.get(1));
    }

    @DisplayName("방 이름으로 id 가져오기 테스트")
    @Test
    void getRoomIdTest() {
        chessService.makeRoom("room1");
        assertEquals(1, chessService.getRoomId("room1"));
    }
}
