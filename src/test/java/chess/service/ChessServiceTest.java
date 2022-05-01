package chess.service;


import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.BoardDao;
import chess.dao.RoomDao;
import chess.domain.Team;
import chess.domain.state.BoardInitialize;
import chess.dto.response.BoardDto;
import chess.dto.request.CreateRoomDto;
import chess.dto.response.GameStateDto;
import chess.dto.response.PieceDto;
import chess.dto.response.RoomDto;
import chess.dto.response.ScoreDto;
import chess.dto.response.StatusDto;
import chess.entity.Room;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChessServiceTest {

    BoardDao boardDao;
    RoomDao roomDao;

    ChessService chessService;

    @BeforeEach
    void setUp() {
        boardDao = new FakeBoardDaoImpl();
        roomDao = new FakeRoomDaoImpl();

        Room room1 = new Room(1L, Team.WHITE, "title", "password", true);
        Room room2 = new Room(2L, Team.WHITE, "title", "password", true);
        roomDao.save("title", "password");
        roomDao.save("title", "password");

        boardDao.saveAll(BoardInitialize.create(), room1.getId());
        boardDao.saveAll(BoardInitialize.create(), room2.getId());

        chessService = new ChessService(boardDao, roomDao);
    }

    @Test
    void findRoomList() {
        List<RoomDto> rooms = chessService.findRoomList();
        assertThat(rooms.size()).isEqualTo(2);
    }

    @Test
    void createRoom() {
        CreateRoomDto room = new CreateRoomDto("title", "password");
        chessService.createRoom(room);
    }

    @Test
    void getPiecesBy() {
        BoardDto board = chessService.getBoard(2L);
        List<PieceDto> pieces = board.getPieces();
        assertThat(pieces.size()).isEqualTo(64);
    }

    @Test
    void getScoreBy() {
        ScoreDto scoreDto = chessService.getScoreBy(2L);
        assertThat(scoreDto.getBlackScore()).isEqualTo(38);
    }

    @Test
    void restartBy() {
        BoardDto boardDto = chessService.resetBy(2L);
        assertThat(boardDto.getPieces().size()).isEqualTo(64);
    }

    @Test
    void findGameStateBy() {
        GameStateDto gameStateDto = chessService.findGameStateBy(2L);
        assertThat(gameStateDto.isRunning()).isTrue();
    }

    @Test
    void endBy() {
        chessService.endBy(2L);
        GameStateDto gameStateDto = chessService.findGameStateBy(2L);
        assertThat(gameStateDto.isRunning()).isFalse();
    }

    @Test
    void move() {
        chessService.move(2L, "a2", "a3");
        BoardDto board = chessService.getBoard(2L);
        List<PieceDto> pieces = board.getPieces();
        assertThat(pieces.size()).isEqualTo(64);
    }

    @Test
    void status() {
        StatusDto statusDto = chessService.getStatus(1L);
        assertThat(statusDto.isRunning()).isTrue();
    }
}
