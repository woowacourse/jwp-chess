package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.dao.GameEntity;
import chess.dao.InMemoryGameDao;
import chess.dao.InMemoryPieceDao;
import chess.dao.PieceEntity;
import chess.service.dto.GameResultDto;
import chess.service.dto.GamesDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChessServiceTest {
    private ChessService service;
    private InMemoryPieceDao boardDao;
    private InMemoryGameDao gameDao;

    @BeforeEach
    void setUp() {
        boardDao = new InMemoryPieceDao();
        gameDao = new InMemoryGameDao();
        service = new ChessService(boardDao, gameDao);
        gameDao.createGame("firstGame", "password");
    }

    @Test
    void createGame() {
        service.createGame("secondGame", "password");
        assertThat(gameDao.getGameTable().size()).isEqualTo(2);
    }

    @Test
    void initGame() {
        service.initGame(1);
        assertThat(gameDao.getGameTable().size()).isEqualTo(1);
        assertThat(boardDao.getBoardByGameId(1).size()).isEqualTo(64);
    }

    @Test
    void move() {
        service.initGame(1);
        service.move(1, "a2", "a4");
        GameEntity gameEntity = gameDao.getGameTable().get(1);
        List<PieceEntity> boardDto = boardDao.getBoardTable().get(1);
        boolean fromSquareIsEmpty = boardDto.stream()
                .anyMatch(piece -> piece.getSquare().equals("a2") && piece.getType().equals("EMPTY"));
        boolean toSquareIsPawn = boardDto.stream()
                .anyMatch(piece -> piece.getSquare().equals("a4") && piece.getType().equals("PAWN"));
        assertThat(fromSquareIsEmpty && toSquareIsPawn).isTrue();
    }

    @Test
    void endGame() {
        service.initGame(1);
        service.endGame(1);
        String status = gameDao.getGameTable().get(1).getStatus();
        assertThat(status).isEqualTo("END");
    }

    @Test
    void getResult() {
        service.initGame(1);
        GameResultDto result = service.getResult(1);
        assertThat(result.getIsDraw()).isTrue();
        assertThat(result.getPlayerPoints().get("WHITE")).isEqualTo(38.0);
        assertThat(result.getPlayerPoints().get("BLACK")).isEqualTo(38.0);
    }

    @Test
    void getAllGames() {
        service.initGame(1);
        GamesDto allGames = service.getAllGames();
        assertThat(allGames.getGames().size()).isEqualTo(1);
    }

    @Test
    void deleteGame() {
        int affectedGames = service.deleteGame(1);
        int remainGames = service.getAllGames().getGames().size();
        assertAll(() -> {
            assertThat(affectedGames).isOne();
            assertThat(remainGames).isZero();
        });
    }
}
