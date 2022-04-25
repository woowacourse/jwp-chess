package web.dao;

import static chess.position.File.A;
import static chess.position.File.B;
import static chess.position.File.C;
import static chess.position.File.D;
import static chess.position.Rank.EIGHT;
import static chess.position.Rank.SEVEN;
import static chess.position.Rank.SIX;
import static org.assertj.core.api.Assertions.assertThat;

import chess.Score;
import chess.piece.Color;
import chess.position.Position;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import web.dto.GameStatus;
import web.dto.PieceDto;
import web.dto.PieceType;

@JdbcTest
class PieceDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private PieceDao pieceDao;
    private ChessGameDao chessGameDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS piece");
        jdbcTemplate.execute("DROP TABLE IF EXISTS chess_game");
        jdbcTemplate.execute("CREATE TABLE chess_game\n"
                + "(\n"
                + "    id            INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,\n"
                + "    name          VARCHAR(10) NOT NULL,\n"
                + "    status        VARCHAR(10) NOT NULL,\n"
                + "    current_color CHAR(5)     NOT NULL,\n"
                + "    black_score   VARCHAR(10) NOT NULL,\n"
                + "    white_score   VARCHAR(10) NOT NULL\n"
                + ")");
        jdbcTemplate.execute("CREATE TABLE piece\n"
                + "(\n"
                + "    position      CHAR(2)     NOT NULL,\n"
                + "    chess_game_id INT         NOT NULL,\n"
                + "    color         CHAR(5)     NOT NULL,\n"
                + "    type          VARCHAR(10) NOT NULL,\n"
                + "    PRIMARY KEY (position, chess_game_id),\n"
                + "    FOREIGN KEY (chess_game_id) REFERENCES chess_game (id)\n"
                + ")");

        chessGameDao = new ChessGameDao(jdbcTemplate);
        chessGameDao.saveChessGame("chess", GameStatus.RUNNING, Color.WHITE,
                new Score(BigDecimal.ONE), new Score(BigDecimal.ONE));

        pieceDao = new PieceDao(jdbcTemplate);
        pieceDao.deleteAll(getChessGameId());
        pieceDao.savePieces(getChessGameId(), List.of(
                new PieceDto(new Position(A, EIGHT), PieceType.KING, Color.WHITE),
                new PieceDto(new Position(A, SEVEN), PieceType.QUEEN, Color.WHITE),
                new PieceDto(new Position(B, EIGHT), PieceType.PAWN, Color.WHITE),
                new PieceDto(new Position(C, EIGHT), PieceType.BISHOP, Color.WHITE),
                new PieceDto(new Position(D, EIGHT), PieceType.KING, Color.BLACK),
                new PieceDto(new Position(D, SEVEN), PieceType.KNIGHT, Color.BLACK),
                new PieceDto(new Position(D, SIX), PieceType.PAWN, Color.BLACK)
        ));
    }

    private int getChessGameId() {
        return chessGameDao.findAll().get(0).getId();
    }

    @AfterEach
    void tearDown() {
        pieceDao.deleteAll(getChessGameId());
    }

    @Test
    void findPieces() {
        assertThat(pieceDao.findPieces(getChessGameId())).containsExactlyInAnyOrder(
                new PieceDto(new Position(A, EIGHT), PieceType.KING, Color.WHITE),
                new PieceDto(new Position(A, SEVEN), PieceType.QUEEN, Color.WHITE),
                new PieceDto(new Position(B, EIGHT), PieceType.PAWN, Color.WHITE),
                new PieceDto(new Position(C, EIGHT), PieceType.BISHOP, Color.WHITE),
                new PieceDto(new Position(D, EIGHT), PieceType.KING, Color.BLACK),
                new PieceDto(new Position(D, SEVEN), PieceType.KNIGHT, Color.BLACK),
                new PieceDto(new Position(D, SIX), PieceType.PAWN, Color.BLACK)
        );
    }

    @Test
    void deletePieceByPosition() {
        pieceDao.deletePieceByPosition(getChessGameId(), new Position(A, SEVEN));
        pieceDao.deletePieceByPosition(getChessGameId(), new Position(D, SIX));
        assertThat(pieceDao.findPieces(getChessGameId())).containsExactlyInAnyOrder(
                new PieceDto(new Position(A, EIGHT), PieceType.KING, Color.WHITE),
                new PieceDto(new Position(B, EIGHT), PieceType.PAWN, Color.WHITE),
                new PieceDto(new Position(C, EIGHT), PieceType.BISHOP, Color.WHITE),
                new PieceDto(new Position(D, EIGHT), PieceType.KING, Color.BLACK),
                new PieceDto(new Position(D, SEVEN), PieceType.KNIGHT, Color.BLACK)
        );
    }
}