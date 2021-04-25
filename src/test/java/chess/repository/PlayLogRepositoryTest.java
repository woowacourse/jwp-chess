package chess.repository;

import chess.domain.board.Board;
import chess.domain.chessgame.ChessGame;
import chess.dto.web.BoardDto;
import chess.dto.web.GameStatusDto;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestPropertySource("classpath:application.properties")
class PlayLogRepositoryTest {

    private final PlayLogRepository playLogRepository;
    private final JdbcTemplate jdbcTemplate;
    private final Gson gson;

    private BoardDto boardDto;
    private GameStatusDto gameStatusDto;

    public PlayLogRepositoryTest(JdbcTemplate jdbcTemplate, Gson gson) {
        playLogRepository = new PlayLogRepository(jdbcTemplate, gson);
        this.jdbcTemplate = jdbcTemplate;
        this.gson = gson;
    }

    @BeforeEach
    void setUp() {
        Board board = new Board();
        ChessGame chessGame = new ChessGame(board);
        boardDto = new BoardDto(board);
        gameStatusDto = new GameStatusDto(chessGame);

        String query = "INSERT INTO play_log (board, game_status, room_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, gson.toJson(boardDto), gson.toJson(gameStatusDto), "1");
    }

    @DisplayName("insert를 하면, 테이블에 게임정보가 들어간다.")
    @Test
    void insert() {
        Board board = new Board();
        ChessGame chessGame = new ChessGame(board);
        BoardDto boardDto = new BoardDto(board);
        GameStatusDto gameStatusDto = new GameStatusDto(chessGame);
        playLogRepository.insert(boardDto, gameStatusDto, "2");
    }

    @DisplayName("보드 정보를 요청하면, roomId에 매칭되는 최근 보드의 정보를 가져온다. ")
    @Test
    void latestBoard() {
        BoardDto boardDto = playLogRepository.latestBoard("1");
        assertThat(boardDto).usingRecursiveComparison().isEqualTo(this.boardDto);
    }

    @DisplayName("게임 상태를 요청하면, roomId에 매칭되는 게임 상태를 가져온다")
    @Test
    void latestGameStatus() {
        GameStatusDto gameStatusDto = playLogRepository.latestGameStatus("1");
        assertThat(gameStatusDto).usingRecursiveComparison().isEqualTo(this.gameStatusDto);
    }
}