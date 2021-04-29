package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.board.ChessBoard;
import chess.domain.game.ChessGame;
import chess.domain.piece.Color;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@JdbcTest
@ActiveProfiles("test")
public class ChessRepositoryTest {

    private ChessRepository chessRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessRepository = new ChessRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("새로운 게임 추가")
    void addGame() {
        Long id = chessRepository.addGame(new ChessGame(), "test");

        ChessGame chessGame = chessRepository.loadGame(id);

        assertThat(chessGame.getId()).isEqualTo(id);
        assertThat(chessGame.getTitle()).isEqualTo("test");
    }

    @Test
    @DisplayName("id로 방 찾아오기")
    void loadGame() {
        ChessGame chessGame = new ChessGame(1L, Color.WHITE, false, new ChessBoard(), "test");
        Long id = chessRepository.addGame(chessGame, "test");

        ChessGame loadedGame = chessRepository.loadGame(id);

        assertThat(chessGame.getTitle()).isEqualTo(loadedGame.getTitle());
        assertThat(id).isEqualTo(loadedGame.getId());
    }

    @Test
    @DisplayName("방 이름으로 찾기")
    void findGame() {
        Long id = chessRepository.addGame(new ChessGame(), "test");
        Optional<Long> findId = chessRepository.findGame("test");
        assertThat(id).isEqualTo(findId.get());
    }

    @Test
    @DisplayName("바뀐 게임 상태 저장과 확인")
    void saveGame() {
        Long id = chessRepository.addGame(new ChessGame(), "test");

        ChessGame chessGame = new ChessGame(new ChessBoard(), Color.BLACK);
        chessRepository.saveGame(id, chessGame);

        ChessGame savedGame = chessRepository.loadGame(id);
        assertThat(savedGame.getTurn()).isEqualTo("BLACK");
    }

    @Test
    @DisplayName("실행 중인 게임 수동 종료")
    void finish() {
        Long id = chessRepository.addGame(new ChessGame(), "test");
        chessRepository.finish(id);

        ChessGame finishedGame = chessRepository.loadGame(id);
        assertThat(finishedGame.isFinished()).isTrue();
    }

    @Test
    @DisplayName("게임 재시작")
    void restart() {
        Long id = chessRepository.addGame(new ChessGame(), "test");
        chessRepository.finish(id);
        chessRepository.restart(id, new ChessGame());

        ChessGame newGame = chessRepository.loadGame(id);
        assertThat(newGame.isFinished()).isFalse();
    }

    @Test
    void findAllGames() {
        chessRepository.addGame(new ChessGame(), "test1");
        chessRepository.addGame(new ChessGame(), "test2");
        chessRepository.addGame(new ChessGame(), "test3");

        List<ChessGame> chessGames = chessRepository.findAllGames();
        assertThat(chessGames).hasSize(3);
        assertThat(chessGames.get(0).getTitle()).isEqualTo("test1");
        assertThat(chessGames.get(1).getTitle()).isEqualTo("test2");
        assertThat(chessGames.get(2).getTitle()).isEqualTo("test3");
    }
}
