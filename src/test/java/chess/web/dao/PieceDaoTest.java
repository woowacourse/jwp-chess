package chess.web.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.board.Turn;
import chess.board.piece.Piece;
import chess.board.piece.Pieces;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
class PieceDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private PieceDao pieceDao;
    private BoardDao boardDao;
    private Pieces pieces;

    @BeforeEach
    void setUp() {
        pieceDao = new PieceDaoImpl(jdbcTemplate);
        boardDao = new BoardDaoImpl(jdbcTemplate);
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("새로운 type과 team으로 바꾸면, db에 저장되고 다시 불러왔을 때 바뀐 type과 team이 나온다.")
    void updatePieceByPositionAndBoardId() {
        pieces = Pieces.createInit();
        Long id = boardDao.save(Turn.init().getTeam().value(), "title", "password");
        pieceDao.save(pieces.getPieces(), id);

        Piece piece = pieces.getPieces().get(0);
        String newType = "king";
        String newTeam = "black";

        pieceDao.updatePieceByPositionAndBoardId(newType, newTeam, piece.getPosition().name(), id);
        List<Piece> updatedPieces = pieceDao.findAllByBoardId(id);
        Piece updatedPiece = updatedPieces.get(0);

        assertAll(
                () -> assertThat(updatedPiece.getType()).isEqualTo(newType),
                () -> assertThat(updatedPiece.getTeam().value()).isEqualTo(newTeam)
        );
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("초기값인 체스말 64개가 모두 조회 되야한다.")
    void findAllByBoardId() {
        pieces = Pieces.createInit();
        Long boardId = boardDao.save(Turn.init().getTeam().value(), "title", "password");
        pieceDao.save(pieces.getPieces(), boardId);

        //when
        List<Piece> pieces = pieceDao.findAllByBoardId(boardId);
        //then
        assertThat(pieces.size()).isEqualTo(64);
    }

}
