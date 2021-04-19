package chess.web.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.chess.Chess;
import chess.domain.chess.Color;
import chess.domain.chess.Status;
import chess.web.dto.ChessDto;
import chess.web.service.ChessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class ChessDaoTest {

    @Autowired
    private ChessDao chessDAO;

    @Autowired
    private ChessService chessService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long chessId;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS piece");
        jdbcTemplate.execute("DROP TABLE IF EXISTS chess;");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS chess (" +
                "                           chess_id BIGINT AUTO_INCREMENT," +
                "                           status   varchar(10)," +
                "                           turn     varchar(10)," +
                "                           PRIMARY KEY (chess_id))");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS piece (" +
                "                           piece_id BIGINT AUTO_INCREMENT," +
                "                           chess_id BIGINT," +
                "                           position varchar(10)," +
                "                           color    varchar(10)," +
                "                           name     varchar(10)," +
                "                           PRIMARY KEY (piece_id)," +
                "                           FOREIGN KEY (chess_id) REFERENCES chess (chess_id));");

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
        assertThat(chessDTO.getBoarDto()
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
