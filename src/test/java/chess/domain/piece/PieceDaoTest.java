package chess.domain.piece;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.board.BoardDto;
import chess.domain.chess.Chess;
import chess.domain.chess.ChessDao;
import chess.domain.chess.ChessDto;
import chess.domain.chess.Color;
import chess.domain.position.MovePosition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PieceDaoTest {

    private final ChessDao chessDao;
    private final PieceDao pieceDao;
    private final long chessId;

    @Autowired
    public PieceDaoTest(ChessDao chessDao, PieceDao pieceDao) {
        this.chessDao = chessDao;
        this.pieceDao = pieceDao;
        chessId = chessDao.insert();
    }

    @DisplayName("초기 기물 삽입 테스트")
    @Test
    void insertTest() {

        // given
        final Chess chess = chessDao.findChessById(chessId);
        final ChessDto chessDTO = new ChessDto(chess);
        assertThat(chessDTO.getBoarDto()
                .getPieceDtos()).size()
                .isEqualTo(0);

        // when
        final Chess newChess = Chess.createWithEmptyBoard().start();
        final BoardDto boardDTO = BoardDto.from(newChess);
        pieceDao.insert(chessId, boardDTO);

        // then
        final Chess insertedChess = chessDao.findChessById(chessId);
        final ChessDto insertedChessDto = new ChessDto(insertedChess);
        assertThat(insertedChessDto.getBoarDto()
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

        final Chess newChess = Chess.createWithEmptyBoard().start();
        pieceDao.insert(chessId, BoardDto.from(newChess));

        // when
        pieceDao.move(chessId, movePosition);

        // then
        final Chess chess = chessDao.findChessById(chessId);
        final ChessDto chessDto = new ChessDto(chess);
        for (PieceDto pieceDto : chessDto.getBoarDto().getPieceDtos()) {
            if (pieceDto.getPosition().equals(source)) {
                assertThat(pieceDto.getPosition()).isEqualTo(source);
                assertThat(pieceDto.getColor()).isEqualTo(Color.BLANK.name());
                assertThat(pieceDto.getName()).isEqualTo(Color.BLANK.name());
            }

            if (pieceDto.getPosition().equals(target)) {
                assertThat(pieceDto.getPosition()).isEqualTo(target);
                assertThat(pieceDto.getColor()).isEqualTo(Color.WHITE.name());
                assertThat(pieceDto.getName()).isEqualTo(Pawn.WHITE_INSTANCE.getName());
            }
        }
    }
}
