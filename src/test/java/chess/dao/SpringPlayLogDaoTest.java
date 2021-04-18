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
import com.google.gson.Gson;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource("classpath:application.properties")
@SpringBootTest
public class SpringPlayLogDaoTest {

    private static final Gson GSON = new Gson();

    private SpringPlayLogDao springPlayLogDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DROP TABLE play_logs");

        jdbcTemplate.update("CREATE TABLE play_logs ("
            + " id int NOT NULL PRIMARY KEY AUTO_INCREMENT,"
            + " board json NOT NULL,"
            + " game_status json NOT NULL,"
            + " room_id int NOT NULL,"
            + " last_played_time timestamp default NOW()"
            + ")");

        jdbcTemplate.update("INSERT INTO play_logs (board, game_status, room_id) VALUES (?, ?, ?)",
            GSON.toJson(new BoardDto(new Board())),
            GSON.toJson(new GameStatusDto(new ChessGame(new Board()))),
            "1");

        springPlayLogDao = new SpringPlayLogDao(jdbcTemplate);
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

    @DisplayName("존재하지 않는 id의 방 최근 보드를 요청한 경우 생성 후 반환")
    @Test
    void latestBoardWhenDoesNotHaveId() {
        BoardDto boardDto = springPlayLogDao.latestBoard("2");

        List<PieceDto> expectedBoard = new BoardDto(new Board()).getBoard();
        List<PieceDto> actualBoard = boardDto.getBoard();

        assertThat(actualBoard).hasSize(expectedBoard.size());
        assertThat(actualBoard).usingRecursiveFieldByFieldElementComparator()
            .containsAll(expectedBoard);
    }

    @DisplayName("존재하지 않는 id의 방 최근 게임 상태를 요청한 경우 생성 후 반환")
    @Test
    void latestGameStatusWhenDoesNotHaveId() {
        GameStatusDto gameStatusDto = springPlayLogDao.latestGameStatus("2");

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

        jdbcTemplate.query("SELECT COUNT(*) FROM play_logs WHERE room_id = 1",
            (resultSet, rowNum) ->
                assertThat(resultSet.getInt(1)).isEqualTo(2)
        );
    }
}
