package chess.service;

import chess.entity.PieceEntity;
import chess.model.board.BoardFactory;
import chess.model.dao.GameDao;
import chess.model.dao.PieceDao;
import chess.model.dto.WebBoardDto;
import chess.model.piece.PieceFactory;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql({"/initGames.sql", "/initPieces.sql"})
class ChessMoveServiceTest {

    private final ChessMoveService chessMoveService;
    private final GameDao gameDao;
    private final PieceDao pieceDao;
    private Long gameId;

    @Autowired
    ChessMoveServiceTest(JdbcTemplate jdbcTemplate) {
        gameDao = new GameDao(jdbcTemplate);
        pieceDao = new PieceDao(jdbcTemplate);

        chessMoveService = new ChessMoveService(pieceDao, gameDao, new ChessBoardService(pieceDao, gameDao));
    }

    @BeforeEach
    void beforeEach() {
        gameId = gameDao.saveGame("serviceTestGame", "pw123");
        pieceDao.savePieces(BoardFactory.create(), gameId);
    }

    @Test
    void move() {
        WebBoardDto webBoardDto = chessMoveService.move(
                gameId, "d2", "d4",
                PieceFactory.create("white-p"), PieceFactory.create("none-."));

        List<PieceEntity> pieces = pieceDao.findAllPiecesByGameId(gameId);

        String source = pieces.stream()
                .filter(piece -> piece.getPosition().equals("d2")).findFirst()
                .map(PieceEntity::getName).get();
        String destination = pieces.stream()
                .filter(piece -> piece.getPosition().equals("d4")).findFirst()
                .map(PieceEntity::getName).get();

        Assertions.assertThat(source).isEqualTo("none-.");
        Assertions.assertThat(destination).isEqualTo("white-p");
    }
}