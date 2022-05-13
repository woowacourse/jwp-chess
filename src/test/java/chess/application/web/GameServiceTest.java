package chess.application.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import chess.dto.BoardResponse;
import chess.dto.GameRequest;
import chess.dto.GameResponse;
import chess.dto.MoveRequest;
import chess.dto.ResultResponse;
import chess.dto.StatusResponse;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class GameServiceTest {
    @Autowired
    private GameService gameService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void after() {
        jdbcTemplate.update("delete from PIECE");
        jdbcTemplate.update("delete from GAME");
    }

    @Test
    @DisplayName("새로운 게임을 생성한다.")
    void save() {
        // given
        GameRequest gameRequest = new GameRequest("test", "pwd");

        // when
        Long savedId = gameService.save(gameRequest);

        // then
        GameResponse gameResponse = gameService.findAllGames().stream()
                .filter(it -> savedId.equals(it.getId()))
                .findAny()
                .get();
        assertThat(gameResponse.getTitle()).isEqualTo(gameRequest.getTitle());
    }

    @Test
    @DisplayName("초기 각 진영의 점수는 각각 38점이다.")
    void findStatus() {
        // given
        GameRequest gameRequest = new GameRequest("test", "pwd");
        Long savedId = gameService.save(gameRequest);

        // when
        StatusResponse result = gameService.findStatus(savedId);

        // then
        assertThat(result.getBlackScore()).isEqualTo(38);
        assertThat(result.getWhiteScore()).isEqualTo(38);
    }

    @Test
    @DisplayName("b2에서 b4로 기물을 위치를 이동한다.")
    void update() {
        // given
        GameRequest gameRequest = new GameRequest("test", "pwd");
        Long savedId = gameService.save(gameRequest);

        // when
        MoveRequest moveRequest = new MoveRequest("b2", "b4");
        BoardResponse result = gameService.update(savedId, moveRequest);

        // then
        assertThat(result.getBoard().get("b2").getType()).isEmpty();
        assertThat(result.getBoard().get("b4").getType()).isEqualTo("pawn");
    }

    @Test
    @DisplayName("id에 해당하는 게임을 삭제한다.")
    void delete() {
        // given
        GameRequest gameRequest = new GameRequest("test", "pwd");
        Long savedId = gameService.save(gameRequest);
        gameService.updateStateById(savedId);

        // when
        gameService.delete(savedId, "pwd");

        // then
        Optional<GameResponse> result = gameService.findAllGames()
                .stream().filter(it -> savedId.equals(it.getId()))
                .findAny();
        assertThat(result).isNotPresent();
    }

    @Test
    @DisplayName("틀린 비밀번호로 게임 삭제를 요청하는 경우 IllegalArgumentException이 발생한다.")
    void delete_incorrect_password() {
        // given
        GameRequest gameRequest = new GameRequest("test", "pwd");
        Long savedId = gameService.save(gameRequest);
        gameService.updateStateById(savedId);

        // when & then
        assertThatThrownBy(() -> gameService.delete(savedId, "pwd1"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("진행중인 게임에 대해 삭제를 요청하는 경우 IllegalStateException이 발생한다.")
    void delete_running_game() {
        // given
        GameRequest gameRequest = new GameRequest("test", "pwd");
        Long savedId = gameService.save(gameRequest);

        // when & then
        assertThatThrownBy(() -> gameService.delete(savedId, "pwd"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("모든 게임을 조회한다.")
    void findAllGames() {
        // given
        Long gameId1 = gameService.save(new GameRequest("test1", "pwd1"));
        Long gameId2 = gameService.save(new GameRequest("test2", "pwd2"));

        // when
        List<GameResponse> result = gameService.findAllGames();

        // then
        assertThat(result).hasSize(2)
                .extracting("id", "title")
                .containsExactly(
                        tuple(gameId1, "test1"),
                        tuple(gameId2, "test2")
                );
    }

    @Test
    @DisplayName("게임 id에 해당하는 체스 보드를 조회한다.")
    void findBoardByGameId() {
        // given
        Long savedId = gameService.save(new GameRequest("test", "pwd"));

        // when
        BoardResponse boardResponse = gameService.findBoardByGameId(savedId);

        // then
        assertThat(boardResponse.getBoard()).hasSize(64);
    }

    @Test
    @DisplayName("초기 상태에서 게임을 종료하면 무승부이다.")
    void updateStateById() {
        // given
        Long savedId = gameService.save(new GameRequest("test", "pwd"));

        // when
        ResultResponse result = gameService.updateStateById(savedId);

        // then
        assertThat(result)
                .extracting("winner", "tie")
                .containsExactly("", true);
    }
}
