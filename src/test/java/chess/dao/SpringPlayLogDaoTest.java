package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.spring.SpringPlayLogDao;
import chess.domain.board.Board;
import chess.domain.board.Point;
import chess.domain.chessgame.ChessGame;
import chess.domain.chessgame.ScoreBoard;
import chess.dto.web.BoardDto;
import chess.dto.web.GameStatusDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        Board board = new Board();
        ChessGame chessGame = new ChessGame(board);
        chessGame.start();

        jdbcTemplate.update("INSERT INTO play_log (board, game_status, room_id) VALUES (?, ?, ?)",
            OBJECT_MAPPER.writeValueAsString(new BoardDto(board)),
            OBJECT_MAPPER.writeValueAsString(new GameStatusDto(chessGame)),
            "1");

        springPlayLogDao = new SpringPlayLogDao(OBJECT_MAPPER, jdbcTemplate);
    }

    @DisplayName("해당 방의 최근 보드 정보 조회")
    @Test
    void latestBoard() {
        BoardDto boardDto = springPlayLogDao.latestBoard("1");

        Board board = new Board();
        ChessGame chessGame = new ChessGame(board);
        chessGame.start();

        assertThat(new BoardDto(board)).usingRecursiveComparison()
            .isEqualTo(boardDto);
    }

    @DisplayName("해당 방의 최근 게임 상태 조회")
    @Test
    void latestGameStatus() {
        GameStatusDto gameStatusDto = springPlayLogDao.latestGameStatus("1");

        ChessGame chessGame = new ChessGame(new Board());
        chessGame.start();

        assertThat(gameStatusDto).usingRecursiveComparison()
            .isEqualTo(new GameStatusDto(chessGame));
    }

    @DisplayName("플레이 기록 추가")
    @Test
    void insert() {
        BoardDto boardDto = springPlayLogDao.latestBoard("1");
        GameStatusDto gameStatusDto = springPlayLogDao.latestGameStatus("1");

        Board board = boardDto.toEntity();

        ChessGame chessGame = new ChessGame(gameStatusDto.toTurnEntity(), new ScoreBoard(board),
            gameStatusDto.toGameStateEntity(board));

        chessGame.move(Point.of("a2"), Point.of("a4"));

        springPlayLogDao.insert(new BoardDto(board), new GameStatusDto(chessGame), "1");

        jdbcTemplate.query("SELECT COUNT(*) FROM play_log WHERE room_id = 1",
            (resultSet, rowNum) ->
                assertThat(resultSet.getInt(1)).isEqualTo(2)
        );
    }
}
