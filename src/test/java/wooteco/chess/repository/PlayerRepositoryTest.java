package wooteco.chess.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import wooteco.chess.AbstractChessApplicationTest;
import wooteco.chess.domain.player.Player;
import wooteco.chess.entity.PlayerEntity;

class PlayerRepositoryTest extends AbstractChessApplicationTest {

    @Autowired
    private PlayerRepository playerRepository;

    @DisplayName("생성이 되는가")
    @Test
    void create() {
        int newId = (int)playerRepository.count() + 1;
        playerRepository.save(new PlayerEntity(new Player("woonjang", "password")));
        assertThat(playerRepository.findById(newId)).isNotEmpty();
        assertThat(playerRepository.findById(newId + 1)).isEmpty();
        playerRepository.save(new PlayerEntity(new Player(newId, "hodol", "password")));
        assertThat(playerRepository.findById(newId).get().getUsername()).isEqualTo("hodol");
    }
}
