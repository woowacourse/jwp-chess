package chess.web.dao;

import chess.domain.board.piece.Piece;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@Sql("/data.sql")
@SpringBootTest
class PieceDaoTest {

    @Autowired
    private PieceDao pieceDao;

    private final Long boardId = 1L;

    @Test
    @DisplayName("새로운 type과 team으로 바꾸면, db에 저장되고 다시 불러왔을 때 바뀐 type과 team이 나온다.")
    void updatePieceByPosition() {
        List<Piece> pieces = pieceDao.findAllByBoardId(1L);
        Piece piece = pieces.get(0);
        String newType = "king";
        String newTeam = "black";

        pieceDao.updatePieceByPositionAndBoardId(newType, newTeam, piece.getPosition().name(), boardId);
        List<Piece> updatedPieces = pieceDao.findAllByBoardId(boardId);
        Piece updatedPiece = updatedPieces.get(0);

        assertThat(updatedPiece.getType()).isEqualTo(newType);
        assertThat(updatedPiece.getTeam().value()).isEqualTo(newTeam);
    }

    @Test
    @DisplayName("초기값인 체스말 64개가 모두 조회 되야한다.")
    void findAllByBoardId() {
        //when
        List<Piece> pieces = pieceDao.findAllByBoardId(boardId);
        //then
        assertThat(pieces.size()).isEqualTo(64);
    }

    @Test
    @DisplayName("board 식별자에 따라 piece들을 삭제한다.")
    void deleteByBoardId() {

        //when
        List<Piece> before = pieceDao.findAllByBoardId(boardId);
        pieceDao.deleteByBoardId(boardId);
        List<Piece> board = pieceDao.findAllByBoardId(this.boardId);
        //then
        assertThat(before.size()).isEqualTo(64);
        assertThat(board.size()).isEqualTo(0);

    }
}
