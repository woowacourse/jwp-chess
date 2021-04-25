package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.spring.SpringPlayLogDao;
import chess.domain.board.Board;
import chess.domain.board.Point;
import chess.domain.chessgame.ChessGame;
import chess.domain.chessgame.ScoreBoard;
import chess.dto.web.BoardDto;
import chess.dto.web.GameStatusDto;
import chess.dto.web.PieceDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;


@JdbcTest
public class SpringPlayLogDaoTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private SpringPlayLogDao springPlayLogDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        jdbcTemplate.update("TRUNCATE TABLE play_log");

        jdbcTemplate.update("INSERT INTO play_log (board, game_status, room_id) VALUES (?, ?, ?)",
            OBJECT_MAPPER.writeValueAsString(new BoardDto(new Board())),
            OBJECT_MAPPER.writeValueAsString(new GameStatusDto(new ChessGame(new Board()))),
            "1");

        springPlayLogDao = new SpringPlayLogDao(OBJECT_MAPPER, jdbcTemplate);
    }

    @DisplayName("해당 방의 최근 보드 정보 조회")
    @Test
    void latestBoard() {
        BoardDto boardDto = springPlayLogDao.latestBoard("1");

        List<PieceDto> expectedBoard = new BoardDto(new Board()).getBoard();
        List<PieceDto> actualBoard = boardDto.getBoard();

        assertThat(actualBoard).hasSize(expectedBoard.size());
        assertThat(actualBoard).usingRecursiveFieldByFieldElementComparator()
            .containsAll(expectedBoard);
    }

    @DisplayName("해당 방의 최근 게임 상태 조회")
    @Test
    void latestGameStatus() {
        GameStatusDto gameStatusDto = springPlayLogDao.latestGameStatus("1");

        assertThat(gameStatusDto).usingRecursiveComparison()
            .isEqualTo(new GameStatusDto(new ChessGame(new Board())));
    }

    @DisplayName("플레이 기록 추가")
    @Test
    void insert() {
        BoardDto boardDto = springPlayLogDao.latestBoard("1");
        GameStatusDto gameStatusDto = springPlayLogDao.latestGameStatus("1");

        Board board = boardDto.toEntity();

        ChessGame chessGame = new ChessGame(gameStatusDto.toTurnEntity(), new ScoreBoard(board),
            gameStatusDto.toGameStateEntity(board));

        chessGame.start();
        chessGame.move(Point.of("a2"), Point.of("a4"));

        springPlayLogDao.insert(new BoardDto(board), new GameStatusDto(chessGame), "1");

        jdbcTemplate.query("SELECT COUNT(*) FROM play_log WHERE room_id = 1",
            (resultSet, rowNum) ->
                assertThat(resultSet.getInt(1)).isEqualTo(2)
        );
    }
}
