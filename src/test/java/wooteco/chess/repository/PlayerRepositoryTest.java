package wooteco.chess.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import wooteco.chess.domain.Game;
import wooteco.chess.domain.player.Player;
import wooteco.chess.entity.GameEntity;
import wooteco.chess.entity.PlayerEntity;

@SpringBootTest
@ActiveProfiles("test")
class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    @DisplayName("생성이 되는가")
    @Test
    void create() {
        playerRepository.save(new PlayerEntity(new Player("woonjang", "password")));
        gameRepository.save(new GameEntity(new Game()));
    }
}
