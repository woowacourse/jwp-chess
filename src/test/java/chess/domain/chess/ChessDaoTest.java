package chess.domain.chess;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.piece.PieceDao;
import chess.service.ChessService;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ChessDaoTest {

    private final ChessDao chessDao;
    private final Long chessId;

    @Autowired
    public ChessDaoTest(JdbcTemplate jdbcTemplate) {
        this.chessDao = new ChessDao(jdbcTemplate);
        this.chessId = new ChessService(chessDao, new PieceDao(jdbcTemplate)).insert();
    }

    @DisplayName("체스 아이디로 체스 게임 정보 가져오기 테스트")
    @Test
    void findChessByIdTest() {

        // when
        final Chess chess = chessDao.findChessById(chessId);
        final ChessDto chessDto = new ChessDto(chess);

        // then
        assertThat(chessDto.getStatus()).isEqualTo("RUNNING");
        assertThat(chessDto.getTurn()).isEqualTo("WHITE");
        assertThat(chessDto.getBoardDto()
                           .getPieceDtos()).size()
                                           .isEqualTo(64);
    }

    @DisplayName("초기 체스판 삽입 테스트")
    @Test
    void insertTest() {

        // when
        Long newChessId = chessDao.insert();

        // then
        assertThat(newChessId).isNotEqualTo(chessId);
    }

    @DisplayName("체스 상태 및 현재 턴 변경 테스트")
    @Test
    void updateChessTest() {

        // when
        chessDao.updateChess(chessId, Status.KING_DEAD.name(), Color.BLACK.name());

        // then
        final Chess chess = chessDao.findChessById(chessId);
        final ChessDto chessDto = new ChessDto(chess);
        assertThat(chessDto.getStatus()).isEqualTo("KING_DEAD");
        assertThat(chessDto.getTurn()).isEqualTo("BLACK");
    }
}
