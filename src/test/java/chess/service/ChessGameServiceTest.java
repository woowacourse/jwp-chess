package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.dao.ChessGameDao;
import chess.dao.PieceDao;
import chess.domain.ChessMap;
import chess.domain.generator.BlackGenerator;
import chess.domain.generator.WhiteGenerator;
import chess.domain.player.Player;
import chess.domain.player.Result;
import chess.domain.player.Team;
import chess.dto.request.CreateGameDto;
import chess.dto.request.DeleteGameDto;
import chess.dto.response.ChessGameStatusDto;
import chess.dto.response.PlayerScoreDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class ChessGameServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ChessGameService service;
    private ChessGameDao chessGameDao;
    private PieceDao pieceDao;

    @BeforeEach
    void setUp() {
        chessGameDao = new ChessGameDao(jdbcTemplate);
        pieceDao = new PieceDao(jdbcTemplate);
        service = new ChessGameService(chessGameDao, pieceDao);
    }

    @Test
    @DisplayName("체스 게임을 생성한다.")
    void createNewChessGame() {
        final String gameName = "게임1";
        final CreateGameDto createGameDto = new CreateGameDto(gameName, "1234", "1234");

        final ChessGameStatusDto newChessGame = service.createNewChessGame(createGameDto);

        assertThat(newChessGame.getName()).isEqualTo(gameName);
    }

    @Test
    @DisplayName("비밀번호가 다르면 게임을 생성할 수 없다.")
    void wrongPasswordNewGame() {
        final CreateGameDto createGameDto = new CreateGameDto("게임1", "1234", "1");

        assertThatThrownBy(() -> service.createNewChessGame(createGameDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("입력한 비밀번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("게임 이름이 중복되면 게임을 생성할 수 없다.")
    void duplicateGameName() {
        service.createNewChessGame(new CreateGameDto("게임1", "1234", "1234"));

        assertThatThrownBy(
                () -> service.createNewChessGame(new CreateGameDto("게임1", "1234", "1234")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 게임 이름입니다.");
    }

    @Test
    @DisplayName("진행 중인 체스 게임은 삭제할 수 없다.")
    void canNotDeleteRunningGame() {
        final int gameId = service.createNewChessGame(new CreateGameDto("게임1", "1234", "1234")).getId();
        final DeleteGameDto deleteGameDto = new DeleteGameDto(gameId, "1234");

        assertThatThrownBy(() -> service.deleteGame(deleteGameDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("진행 중인 게임은 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("게임 번호로 체스맵을 조회한다.")
    void loadChessMap() {
        final int gameId = service.createNewChessGame(new CreateGameDto("게임1", "1234", "1234")).getId();
        final Player whitePlayer = new Player(new WhiteGenerator(), Team.WHITE);
        final Player blackPlayer = new Player(new BlackGenerator(), Team.BLACK);
        final char[][] expected = ChessMap.of(whitePlayer.findAll(), blackPlayer.findAll()).getChessMap();

        final char[][] actual = service.loadChessMap(gameId).getChessMap();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("번호로 체스 게임을 조회한다.")
    void findChessGame() {
        final int gameId = service.createNewChessGame(new CreateGameDto("게임1", "1234", "1234")).getId();

        final ChessGameStatusDto gameInfo = service.findGameInfoById(gameId);

        assertAll(() -> {
            assertThat(gameInfo.getId()).isEqualTo(gameId);
            assertThat(gameInfo.getName()).isEqualTo("게임1");
            assertThat(gameInfo.isRunning()).isTrue();
        });
    }

    @Test
    @DisplayName("점수를 조회한다.")
    void findStatus() {
        final CreateGameDto createGameDto = new CreateGameDto("게임1", "1234", "1234");
        final int gameId = service.createNewChessGame(createGameDto).getId();
        final double expectedScore = 38;
        final String expectedResult = Result.DRAW.getValue();

        final PlayerScoreDto status = service.findStatus(gameId);

        assertAll(() -> {
            assertThat(status.getWhitePlayerScore()).isEqualTo(expectedScore);
            assertThat(status.getBlackPlayerScore()).isEqualTo(expectedScore);
            assertThat(status.getWhitePlayerResult()).isEqualTo(expectedResult);
            assertThat(status.getBlackPlayerResult()).isEqualTo(expectedResult);
        });
    }

    @Test
    @DisplayName("모든 체스 게임을 조회한다.")
    void findAllChessGame() {
        service.createNewChessGame(new CreateGameDto("게임1", "1234", "1234"));
        final int expected = 1;

        final int actual = service.findAllChessGame().size();

        assertThat(actual).isEqualTo(expected);
    }
}
