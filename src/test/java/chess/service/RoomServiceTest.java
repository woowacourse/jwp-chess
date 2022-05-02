package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.dao.InMemoryGameDao;
import chess.dao.InMemoryPieceDao;
import chess.repository.BoardRepository;
import chess.repository.ChessGameRepository;
import chess.repository.RoomRepository;
import chess.service.dto.response.DeleteGameResponse;
import chess.service.dto.response.RoomsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;

class RoomServiceTest {

    private RoomService service;
    private InMemoryGameDao gameDao;

    @BeforeEach
    void setUp() {
        InMemoryPieceDao pieceDao = new InMemoryPieceDao();
        gameDao = new InMemoryGameDao();
        BoardRepository boardRepository = new BoardRepository(pieceDao);
        ChessGameRepository chessGameRepository = new ChessGameRepository(gameDao, boardRepository);
        RoomRepository roomRepository = new RoomRepository(gameDao);
        service = new RoomService(gameDao, chessGameRepository, roomRepository);
        gameDao.createGame("first", BCrypt.hashpw("password", BCrypt.gensalt()));
    }

    @Test
    void getAllRooms() {
        RoomsDto allGames = service.getAllRooms();
        assertThat(allGames.getRooms().size()).isEqualTo(1);
    }

    @Test
    void deleteRoom() {
        DeleteGameResponse affectedGames = service.deleteRoom(1, "password");
        int remainGames = service.getAllRooms().getRooms().size();
        assertAll(() -> {
            assertThat(affectedGames.isSuccess()).isTrue();
            assertThat(remainGames).isZero();
        });
    }

    @Test
    void createRoom() {
        service.createRoom("secondGame", "password");
        assertThat(gameDao.getGameTable().size()).isEqualTo(2);
    }
}
