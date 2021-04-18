package chess.dao;

import chess.domain.board.Board;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@JdbcTest
class PieceDAOTest {

    private PieceDAO pieceDAO;
    private ChessGameDAO chessGameDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        pieceDAO = new PieceDAO(jdbcTemplate);
        chessGameDAO = new ChessGameDAO(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("DELETE FROM piece");
        jdbcTemplate.execute("DELETE FROM chess_game");
    }

    @DisplayName("piece들을 저장하고 조회하는 기능을 테스트한다")
    @Test
    void testSaveAllAndFindAllPiecesByChessGameId() {
        //given
        Long chessGameId = chessGameDAO.save();
        List<Piece> pieces = Arrays.asList(
                Piece.createKing(Color.BLACK, 0, 0),
                Piece.createKing(Color.WHITE, 7, 0)
        );

        //when
        pieceDAO.saveAll(chessGameId, pieces);

        //then
        List<Piece> findPieces = pieceDAO.findAllPiecesByChessGameId(chessGameId);
        assertAll(
                () -> assertThat(findPieces).hasSize(2),
                () -> assertThat(findPieces).containsExactlyInAnyOrder(
                        Piece.createKing(Color.BLACK, 0, 0),
                        Piece.createKing(Color.WHITE, 7, 0)
                )
        );
    }

    @DisplayName("chessGameId를 가지고 모든 Piece들을 조회하는 기능을 테스트한다 ")
    @Test
    void testFindOneByPosition() {
        //given
        Long chessGameId = chessGameDAO.save();
        Piece piece = Piece.createKing(Color.WHITE, 7, 0);
        List<Piece> pieces = Arrays.asList(piece);
        pieceDAO.saveAll(chessGameId, pieces);

        //when
        Piece findPiece = pieceDAO.findOneByPosition(chessGameId, 7, 0).get();

        //then
        assertThat(findPiece).isEqualTo(piece);
    }

    @DisplayName("Piece를 업데이트하는 기능을 테스트한다 ")
    @Test
    void testUpdate() {
        //given
        Long chessGameId = chessGameDAO.save();
        List<Piece> pieces = Arrays.asList(Piece.createKing(Color.WHITE, 7, 0));
        pieceDAO.saveAll(chessGameId, pieces);
        Piece savedPiece = pieceDAO.findOneByPosition(chessGameId, 7, 0).get();
        savedPiece.move(new Position(6, 0), new Board(Collections.emptyList()));

        //when
        pieceDAO.update(savedPiece);

        //then
        Piece findPiece = pieceDAO.findOneByPosition(chessGameId, 6, 0).get();
        assertAll(
                () -> assertThat(findPiece).isNotNull(),
                () -> assertThat(findPiece.isSameColor(Color.WHITE)),
                () -> assertThat(findPiece.isSamePosition(new Position(6, 0))).isTrue()
        );
    }

    @DisplayName("Piece를 삭제하는 기능을 테스트한다 ")
    @Test
    void testDelete() {
        //given
        Long chessGameId = chessGameDAO.save();
        List<Piece> pieces = Arrays.asList(Piece.createKing(Color.WHITE, 7, 0));
        pieceDAO.saveAll(chessGameId, pieces);

        //when
        pieceDAO.delete(chessGameId, 7, 0);

        //then
        List<Piece> findPieces = pieceDAO.findAllPiecesByChessGameId(chessGameId);
        assertThat(findPieces).isEmpty();
    }
}
