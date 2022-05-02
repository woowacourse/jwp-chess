package chess.dao;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import chess.domain.game.LogIn;
import chess.domain.piece.ChessmenInitializer;
import chess.domain.piece.Color;
import chess.domain.piece.King;
import chess.domain.piece.Piece;
import chess.domain.piece.Pieces;
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
    private static final LogIn LOG_IN_DTO = new LogIn("1234", "1234");

    private PieceDao pieceDao;
    private GameDao gameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameDao = new GameDao(jdbcTemplate);
        pieceDao = new PieceDao(jdbcTemplate);
    }

    @DisplayName("createAll로 Piece 리스트를 생성한다")
    @Test
    void createAll() {
        //given
        gameDao.create(LOG_IN_DTO);
        //when
        pieceDao.createAll(new ChessmenInitializer().init(), "1234");

        //then
        assertThat(pieceDao.findAll("1234").getPieces().size())
                .isEqualTo(32);
    }

    @DisplayName("updateAll로 해당 아이디의 Piece들의 상태를 업데이트한다")
    @Test
    void updateAll() {
        //given
        gameDao.create(LOG_IN_DTO);
        final Pieces pieces = new ChessmenInitializer().init();
        pieceDao.createAll(pieces, "1234");

        List<Piece> pieceList = pieces.getPieces();
        pieceList.remove(pieces.size() - 1);
        pieceList.add(new King(Color.BLACK, Position.of("h2")));

        //when
        pieceDao.updateAll(pieceList, "1234");

        //then
        assertThat(pieceDao.findAll("1234")
                .extractPiece(Position.of("h2"))
                .getName())
                .isEqualTo("king");
    }

    @DisplayName("findAll 로 해당 아이디의 Piece들을 반환한다")
    @Test
    void findAll() {
        //given
        gameDao.create(LOG_IN_DTO);
        pieceDao.createAll(new ChessmenInitializer().init(), "1234");

        //when
        Pieces pieces = pieceDao.findAll("1234");

        //then
        assertThat(pieces.getPieces().size()).isEqualTo(32);
    }

    @DisplayName("deleteAll 로 해당 아이디의 Piece들의 상태를 제거한다")
    @Test
    void deleteAll() {
        //given
        gameDao.create(LOG_IN_DTO);
        pieceDao.createAll(new ChessmenInitializer().init(), "1234");

        //when
        pieceDao.deleteAll("1234");

        //then
        assertThat(pieceDao.findAll("1234").getPieces().size()).isEqualTo(0);
    }
}
