package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.InMemoryGameDao;
import chess.dao.InMemoryPieceDao;
import chess.repository.BoardRepository;
import chess.repository.ChessGameRepository;
import chess.repository.dao.entity.PieceEntity;
import chess.service.dto.response.GameResultDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChessServiceTest {
    private ChessService service;
    private InMemoryPieceDao pieceDao;
    private InMemoryGameDao gameDao;

    @BeforeEach
    void setUp() {
        pieceDao = new InMemoryPieceDao();
        gameDao = new InMemoryGameDao();
        service = new ChessService(new ChessGameRepository(gameDao, new BoardRepository(pieceDao)),
                new BoardRepository(pieceDao));
        gameDao.createGame("first", "password");
    }

    @Test
    void initGame() {
        service.initGame(1);
        assertThat(gameDao.getGameTable().size()).isEqualTo(1);
        assertThat(pieceDao.getBoardByGameId(1).size()).isEqualTo(64);
    }

    @Test
    void move() {
        service.initGame(1);
        service.move(1, "a2", "a4");
        List<PieceEntity> boardDto = pieceDao.getBoardTable().get(1);
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
}
