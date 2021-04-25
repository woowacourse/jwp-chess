package chess.service;

import chess.domain.piece.Color;
import chess.dto.NewGameDto;
import chess.dto.RunningGameDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ChessGameServiceTest {
    @Autowired
    private ChessGameService chessGameService;

    @Value("${spring.datasource.driver-class-name}")
    String driver;
    @Value("${spring.datasource.url}")
    String dataSourceUrl;
    @Value("${spring.datasource.username}")
    String userName;
    @Value("${spring.datasource.password}")
    String userPassword;

    @Test
    @DisplayName("새로운 게임을 생성한다.")
    void createNewGameTest() {
        // when
        NewGameDto newGameDto = chessGameService.createNewGame();

        // then
        assertThat(newGameDto.getChessBoard()).isInstanceOf(Map.class);
        assertThat(newGameDto.getCurrentTurnColor()).isInstanceOf(Color.class);
    }

    @Test
    @DisplayName("게임의 고유 값으로 게임을 읽어온다.")
    void loadChessGameBByGameId() {
        // given
        int gameId = chessGameService.createNewGame().getGameId();

        chessGameService.loadChessGameByGameId(gameId);

        // then
    }

    @Test
    @DisplayName("기물을 이동한 결과를 반환한다.")
    void moveTest() {
        // given
        int gameId = chessGameService.createNewGame().getGameId();

        // when
        RunningGameDto runningGameDto = chessGameService.move(gameId, "a2", "a4");

        // then
        assertThat(runningGameDto.getChessBoard().get("a4")).isNotNull();
        assertThat(runningGameDto.getCurrentTurnColor()).isEqualTo(Color.BLACK);
    }

    @Test
    @DisplayName("게임들의 목록을 읽어온다.")
    void loadAllGamesTest() {
        int NUMBER_OF_GAME = 3;
        for (int i = 0; i < NUMBER_OF_GAME; i++) {
            chessGameService.createNewGame();
        }

        assertThat(chessGameService.loadAllGames().getGamesId()).hasSize(NUMBER_OF_GAME);
    }

    @AfterEach
    void flush() {
        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(dataSourceUrl, userName, userPassword);
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM game");
            statement.close();
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

    }
}