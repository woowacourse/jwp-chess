package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.dao.InMemoryBoardDao;
import chess.dao.InMemoryGameDao;
import chess.service.dto.BoardDto;
import chess.service.dto.ChessGameDto;
import chess.service.dto.GameResultDto;
import chess.service.dto.GamesDto;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class ChessServiceTest {
    private ChessService service;
    private InMemoryBoardDao boardDao;
    private InMemoryGameDao gameDao;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        boardDao = new InMemoryBoardDao();
        gameDao = new InMemoryGameDao();
        passwordEncoder = new BCryptPasswordEncoder();
        service = new ChessService(boardDao, gameDao, passwordEncoder);
    }

    @Test
    void createGame() {
        String password = "1a2s3d4f";
        Long gameId = service.createGame("firstGame", password);
        Map<Long, ChessGameDto> gameTable = gameDao.getGameTable();

        assertAll(() -> {
            assertThat(gameTable).hasSize(1);
            assertThat(passwordEncoder.matches(password,
                gameTable.get(gameId).getPassword())).isTrue();
        });
    }

    @Test
    void initGame() {
        service.createGame("firstGame", "1a2s3d4f");
        service.initGame(1L);
        assertThat(gameDao.getGameTable().size()).isEqualTo(1);
        assertThat(boardDao.getBoardByGameId(1L).getPieces().size()).isEqualTo(64);
    }

    @Test
    void move() {
        service.createGame("firstGame", "1a2s3d4f");
        service.initGame(1L);
        service.move(1L, "a2", "a4");
        ChessGameDto chessGameDto = gameDao.getGameTable().get(1L);
        BoardDto boardDto = boardDao.getBoardTable().get(1L);
        boolean fromSquareIsEmpty = boardDto.getPieces().stream()
                .anyMatch(piece -> piece.getSquare().equals("a2") && piece.getType().equals("EMPTY"));
        boolean toSquareIsPawn = boardDto.getPieces().stream()
                .anyMatch(piece -> piece.getSquare().equals("a4") && piece.getType().equals("PAWN"));
        assertThat(fromSquareIsEmpty && toSquareIsPawn).isTrue();
    }

    @Test
    void isRunning() {
        service.createGame("firstGame", "1a2s3d4f");
        service.initGame(1L);
        assertThat(service.isRunning(1L)).isTrue();
    }

    @Test
    void isGameEmpty() {
        service.createGame("firstGame", "1a2s3d4f");
        service.initGame(1L);
        assertThat(service.isGameEmpty(1L)).isFalse();
    }

    @Test
    void endGame() {
        service.createGame("firstGame", "1a2s3d4f");
        service.initGame(1L);
        service.endGame(1L);
        BoardDto boardDto = boardDao.getBoardTable().get(1L);
        String status = gameDao.getGameTable().get(1L).getStatus();
        assertThat(boardDto).isNull();
        assertThat(status).isEqualTo("EMPTY");
    }

    @Test
    void getResult() {
        service.createGame("firstGame", "1a2s3d4f");
        service.initGame(1L);
        GameResultDto result = service.getResult(1L);
        assertThat(result.getIsDraw()).isTrue();
        assertThat(result.getPlayerPoints().get("WHITE")).isEqualTo(38.0);
        assertThat(result.getPlayerPoints().get("BLACK")).isEqualTo(38.0);
    }

    @Test
    void getAllGames() {
        service.createGame("firstGame", "1a2s3d4f");
        service.initGame(1L);
        GamesDto allGames = service.getAllGames();
        assertThat(allGames.getGames().size()).isEqualTo(1);
    }
}
