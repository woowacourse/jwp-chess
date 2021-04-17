package chess.domain.piece;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.board.BoardDto;
import chess.domain.chess.Chess;
import chess.domain.chess.ChessDao;
import chess.domain.chess.ChessDto;
import chess.domain.chess.Color;
import chess.domain.position.MovePosition;

@SpringBootTest
public class PieceDaoTest {

    private final ChessDao chessDAO;
    private final PieceDao pieceDAO;
    private final long chessId;

    @Autowired
    public PieceDaoTest(ChessDao chessDAO, PieceDao pieceDAO) {
        this.chessDAO = chessDAO;
        this.pieceDAO = pieceDAO;
        chessId = chessDAO.insert();
    }

    @DisplayName("초기 기물 삽입 테스트")
    @Test
    void insertTest() {

        // given
        final Chess chess = chessDAO.findChessById(chessId);
        final ChessDto chessDTO = new ChessDto(chess);
        assertThat(chessDTO.getBoardDTO()
                           .getPieceDtos()).size()
                                           .isEqualTo(0);

        // when
        final Chess newChess = Chess.createWithEmptyBoard()
                                    .start();
        final BoardDto boardDTO = BoardDto.from(newChess);
        pieceDAO.insert(chessId, boardDTO);

        // then
        final Chess insertedChess = chessDAO.findChessById(chessId);
        final ChessDto insertedChessDto = new ChessDto(insertedChess);
        assertThat(insertedChessDto.getBoardDTO()
                                   .getPieceDtos()).size()
                                                   .isEqualTo(64);
    }

    @DisplayName("기물 이동 테스트")
    @Test
    void moveTest() {

        // given
        final String source = "a2";
        final String target = "a4";
        MovePosition movePosition = new MovePosition(source, target);

        final Chess newChess = Chess.createWithEmptyBoard()
                                    .start();
        pieceDAO.insert(chessId, BoardDto.from(newChess));

        // when
        pieceDAO.move(chessId, movePosition);

        // then
        final Chess chess = chessDAO.findChessById(chessId);
        final ChessDto chessDTO = new ChessDto(chess);
        for (PieceDto pieceDTO : chessDTO.getBoardDTO()
                                         .getPieceDtos()) {
            if (pieceDTO.getPosition()
                        .equals(source)) {
                assertThat(pieceDTO.getPosition()).isEqualTo(source);
                assertThat(pieceDTO.getColor()).isEqualTo(Color.BLANK.name());
                assertThat(pieceDTO.getName()).isEqualTo(Color.BLANK.name());
            }

            if (pieceDTO.getPosition()
                        .equals(target)) {
                assertThat(pieceDTO.getPosition()).isEqualTo(target);
                assertThat(pieceDTO.getColor()).isEqualTo(Color.WHITE.name());
                assertThat(pieceDTO.getName()).isEqualTo(Pawn.WHITE_INSTANCE.getName());
            }
        }
    }
}
