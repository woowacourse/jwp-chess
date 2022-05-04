package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.chessboard.ChessBoard;
import chess.domain.piece.Symbol;
import chess.domain.piece.generator.NormalPiecesGenerator;
import chess.entity.Game;
import chess.entity.Square;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class BoardDaoTest {

    @Autowired
    private BoardDao boardDao;

    @Autowired
    private GameDao gameDao;

    private int gameId;

    @BeforeEach
    void setUp() {
        gameId = gameDao.save(new Game("title", "password", "WhiteTurn"));
        boardDao.save(Square.from(new ChessBoard(new NormalPiecesGenerator()), gameId));
    }

    @Test
    @DisplayName("체스 보드를 조회할 수 있다.")
    void findById() {
        var squares = boardDao.findById(gameId);

        assertThat(squares.get(0)).isInstanceOf(Square.class);
    }

    @Test
    @DisplayName("체스 보드를 업데이트할 수 있다.")
    void update() {
        Square square = new Square("a1", "PAWN", "WHITE", gameId);

        boardDao.update(square);
        List<Square> squares = boardDao.findById(gameId);
        Square actual = squares.stream()
                .filter(it -> it.getPosition().equals("a1"))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);

        assertThat(actual.getSymbol()).isEqualTo(Symbol.PAWN.name());
    }

    @Test
    @DisplayName("체스 보드를 삭제할 수 있다.")
    void delete() {
        boardDao.delete(gameId);

        List<Square> squares = boardDao.findById(gameId);
        assertThat(squares).isEmpty();
    }
}
