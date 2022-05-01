package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.chessboard.ChessBoard;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.Symbol;
import chess.domain.piece.generator.NormalPiecesGenerator;
import chess.domain.position.Position;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class BoardDaoTest {

    @Autowired
    private BoardDao boardDao;

    @Autowired
    private GameDao gameDao;

    private int gameId;

    @BeforeEach
    void setUp() {
        gameId = gameDao.save("title", "password", "WhiteTurn");
        boardDao.save(new ChessBoard(new NormalPiecesGenerator()), gameId);
    }

    @Test
    @DisplayName("체스 보드를 조회할 수 있다.")
    void findById() {
        var chessBoard = boardDao.findById(gameId);

        assertThat(chessBoard).isInstanceOf(ChessBoard.class);
    }

    @Test
    @DisplayName("체스 보드를 업데이트할 수 있다.")
    void update() {
        Position position = Position.of("a2");
        Piece piece = Piece.of(Color.BLACK, Symbol.PAWN);

        boardDao.update(position, piece, gameId);
        ChessBoard chessBoard = boardDao.findById(gameId);
        Map<Position, Piece> pieces = chessBoard.getPieces();
        Piece actual = pieces.get(position);

        assertThat(actual).isEqualTo(piece);
    }
}
