package chess.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import chess.dao.BoardDao;
import chess.dao.RoomDao;
import chess.domain.Team;
import chess.domain.state.BoardInitialize;
import chess.dto.BoardDto;
import chess.dto.GameStateDto;
import chess.dto.PieceDto;
import chess.dto.RoomDto;
import chess.dto.ScoreDto;
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
        roomDao.save(room1);
        roomDao.save(room2);

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
        RoomDto room = new RoomDto(3L, Team.WHITE, "title", "password", true);
        chessService.createRoom(room);
    }

    @Test
    void deleteBy() {
        assertThatNoException().isThrownBy(() -> chessService.deleteBy(1L, "password"));
    }

    @Test
    void getPiecesBy() {
        List<PieceDto> pieces = chessService.getPiecesBy(2L);
        assertThat(pieces.size()).isEqualTo(64);
    }

    @Test
    void getScoreBy() {
        ScoreDto scoreDto = chessService.getScoreBy(2L);
        assertThat(scoreDto.getBlackScore()).isEqualTo(38);
    }

    @Test
    void restartBy() {
        BoardDto boardDto = chessService.restartBy(2L);
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
        List<PieceDto> pieces = chessService.getPiecesBy(2L);
    }
}
