package chess.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.FakeChessDao;
import chess.domain.chess.Chess;
import chess.domain.chess.ChessDto;

public class ChessServiceTest {

    private final ChessService chessService;

    public ChessServiceTest() {
        final Map<Long, Chess> fakeChessTable = new HashMap<>();
        fakeChessTable.put(1L, Chess.createWithEmptyBoard().start());
        chessService = new ChessService(new FakeChessDao(fakeChessTable));
    }

    @DisplayName("체스 아이디로 체스 게임 정보 가져오기 테스트")
    @Test
    void getChessGameTest() {

        // given
        final long chessId = 1L;

        // when
        final Chess chess = chessService.findChessById(chessId);
        final ChessDto chessDto = new ChessDto(chess);

        // then
        assertThat(chessDto.getStatus()).isEqualTo("RUNNING");
        assertThat(chessDto.getTurn()).isEqualTo("WHITE");
        assertThat(chessDto.getBoardDto().getPieceDtos()).size().isEqualTo(64);
    }
}
