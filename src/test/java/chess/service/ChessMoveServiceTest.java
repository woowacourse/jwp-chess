package chess.service;

import chess.model.board.BoardFactory;
import chess.model.dao.GameDao;
import chess.model.dao.PieceDao;
import chess.model.dto.WebBoardDto;
import chess.model.piece.Piece;
import chess.model.piece.PieceFactory;
import chess.model.position.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    private static final Long gameId = 1L;

    private final JdbcTemplate jdbcTemplate;
    private final ChessMoveService chessMoveService;

    @Autowired
    ChessMoveServiceTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        PieceDao pieceDao = new PieceDao(jdbcTemplate);
        GameDao gameDao = new GameDao(jdbcTemplate);
        chessMoveService = new ChessMoveService(pieceDao, gameDao, new ChessGameService(pieceDao, gameDao));
    }

    @BeforeEach
    void beforeEach() {
        String sql = "insert into pieces (position, name, game_id) values (?, ?, ?)";

        List<Object[]> batch = new ArrayList<>();
        for (Map.Entry<Position, Piece> entry : BoardFactory.create().getBoard().entrySet()) {
            Object[] values = new Object[]{entry.getKey().getPosition(),
                    (entry.getValue().getTeam().name() + "-" + entry.getValue().getName()).toLowerCase(),
                    gameId};
            batch.add(values);
        }

        jdbcTemplate.batchUpdate(sql, batch);
    }

    @Test
    void move() {
        WebBoardDto webBoardDto = chessMoveService.move(
                gameId, "d2", "d4",
                PieceFactory.create("white-p"), PieceFactory.create("none-."));

        String source = findNameByPositionAndGameId("d2");
        String destination = findNameByPositionAndGameId("d4");

        Assertions.assertThat(source).isEqualTo("none-.");
        Assertions.assertThat(destination).isEqualTo("white-p");
    }

    private String findNameByPositionAndGameId(String position) {
        String sql = "select name from pieces where position = (?) and game_id = (?)";
        return jdbcTemplate.queryForObject(sql, String.class, position, gameId);
    }
}