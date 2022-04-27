package chess.dao;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import chess.domain.piece.ChessmenInitializer;
import chess.domain.piece.Color;
import chess.domain.piece.King;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class PieceDaoTest {

    private PieceDao pieceDao;
    private GameDao gameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameDao = new GameDao(jdbcTemplate);
        pieceDao = new PieceDao(jdbcTemplate);
    }

    @DisplayName("createAllById로 Piece 리스트를 생성한다")
    @Test
    void createAllById() {
        //given
        gameDao.createById("1234");
        //when
        pieceDao.createAllById(new ChessmenInitializer().init().getPieces(), "1234");

        //then
        assertThat(pieceDao.findAll("1234").getPieces().size())
                .isEqualTo(32);
    }

    @DisplayName("updateAllByGameId로 해당 아이디의 Piece들의 상태를 업데이트한다")
    @Test
    void updateAllByGameId() {
        //given
        gameDao.createById("1234");
        final List<Piece> pieces = new ChessmenInitializer().init().getPieces();
        pieceDao.createAllById(pieces, "1234");
        pieces.remove(pieces.size() - 1);
        pieces.add(new King(Color.BLACK, Position.of("h2")));

        //when
        pieceDao.updateAllByGameId(pieces, "1234");

        //then
        assertThat(pieceDao.findAll("1234")
                .extractPiece(Position.of("h2"))
                .getName())
                .isEqualTo("king");
    }

    @DisplayName("deleteAllByGameId로 해당 아이디의 Piece들의 상태를 제거한다")
    @Test
    void deleteAllByGameId() {
        //given
        gameDao.createById("1234");
        pieceDao.createAllById(new ChessmenInitializer().init().getPieces(), "1234");

        //when
        pieceDao.deleteAllByGameId("1234");

        //then
        assertThat(pieceDao.findAll("1234").getPieces().size()).isEqualTo(0);
    }
}
