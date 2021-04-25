package chess.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.FakeChessDao;
import chess.dao.FakeMoveDao;
import chess.domain.board.BoardDto;
import chess.domain.chess.Chess;
import chess.domain.chess.Color;
import chess.domain.chess.Status;
import chess.domain.piece.PieceDto;
import chess.domain.position.MovePosition;

public class ChessServiceTest {

    private final ChessService chessService;

    public ChessServiceTest() {
        final Map<Long, Chess> fakeChessTable = new HashMap<>();
        fakeChessTable.put(1L, Chess.createWithInitializedBoard());
        chessService =
                new ChessService(new FakeChessDao(fakeChessTable), new FakeMoveDao(fakeChessTable));
    }

    @DisplayName("체스 조회 테스트")
    @Test
    void findChessByIdTest() {

        // given
        final long chessId = 1L;

        // when
        final Chess chess = chessService.findChessById(chessId);

        // then
        final List<PieceDto> pieceDtos = BoardDto.from(chess).getPieceDtos();
        assertThat(pieceDtos).size().isEqualTo(64);
        assertThat(chess.status()).isEqualTo(Status.RUNNING.name());
        assertThat(chess.color()).isEqualTo(Color.WHITE.name());
    }

    @DisplayName("체스 상태 및 현재 턴 변경 테스트")
    @Test
    void moveTest() {

        // given
        long chessId = 1L;
        String source = "a2";
        String target = "a4";
        MovePosition movePosition = new MovePosition(source, target);

        // when
        final Chess chess = chessService.move(chessId, movePosition);

        // then
        assertThat(chess.status()).isEqualTo(Status.RUNNING.name());
        assertThat(chess.color()).isEqualTo(Color.BLACK.name());

        final BoardDto boardDto = BoardDto.from(chess);
        for (PieceDto pieceDto : boardDto.getPieceDtos()) {
            if (pieceDto.getPosition().equals(source)) {
                assertThat(pieceDto.getColor()).isEqualTo("BLANK");
                assertThat(pieceDto.getName()).isEqualTo("BLANK");
            }

            if (pieceDto.getPosition().equals(target)) {
                assertThat(pieceDto.getColor()).isEqualTo("WHITE");
                assertThat(pieceDto.getName()).isEqualTo("PAWN");
            }
        }
    }
}
