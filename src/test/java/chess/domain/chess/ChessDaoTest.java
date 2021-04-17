package chess.domain.chess;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

import chess.service.ChessService;

@SpringBootTest
public class ChessDaoTest {

    private final ChessDao chessDAO;
    private final Long chessId;

    @Autowired
    public ChessDaoTest(ChessDao chessDAO, ChessService chessService) {
        this.chessDAO = chessDAO;
        chessId = chessService.insert();
    }

    @DisplayName("체스 아이디로 체스 게임 정보 가져오기 테스트")
    @Test
    void findChessByIdTest() {

        // when
        final Chess chess = chessDAO.findChessById(chessId);
        final ChessDto chessDTO = new ChessDto(chess);

        // then
        assertThat(chessDTO.getStatus()).isEqualTo("RUNNING");
        assertThat(chessDTO.getTurn()).isEqualTo("WHITE");
        assertThat(chessDTO.getBoardDTO()
                           .getPieceDtos()).size()
                                           .isEqualTo(64);
    }

    @DisplayName("초기 체스판 삽입 테스트")
    @Test
    void insertTest() {

        // when
        Long newChessId = chessDAO.insert();

        // then
        assertThat(newChessId).isNotEqualTo(chessId);
    }

    @DisplayName("체스 상태 및 현재 턴 변경 테스트")
    @Test
    void updateChessTest() {

        // when
        chessDAO.updateChess(chessId, Status.KING_DEAD.name(), Color.BLACK.name());

        // then
        final Chess chess = chessDAO.findChessById(chessId);
        final ChessDto chessDTO = new ChessDto(chess);
        assertThat(chessDTO.getStatus()).isEqualTo("KING_DEAD");
        assertThat(chessDTO.getTurn()).isEqualTo("BLACK");
    }
}
