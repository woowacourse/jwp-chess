package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import chess.dao.InMemoryGameDao;
import chess.dao.InMemoryPieceDao;
import chess.repository.ChessGameRepository;
import chess.repository.BoardRepository;
import chess.repository.RoomRepository;
import chess.service.dto.response.DeleteGameResponse;
import chess.service.dto.response.GamesDto;
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
    void getAllGames() {
        GamesDto allGames = service.getAllRooms();
        assertThat(allGames.getGames().size()).isEqualTo(1);
    }

    @Test
    void deleteGame() {
        DeleteGameResponse affectedGames = service.deleteRoom(1, "password");
        int remainGames = service.getAllRooms().getGames().size();
        assertAll(() -> {
            assertThat(affectedGames.isSuccess()).isTrue();
            assertThat(remainGames).isZero();
        });
    }

    @Test
    void createGame() {
        service.createRoom("secondGame", "password");
        assertThat(gameDao.getGameTable().size()).isEqualTo(2);
    }
}
