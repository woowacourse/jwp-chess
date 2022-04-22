package chess.web.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.game.state.Player;
import chess.domain.piece.property.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerDaoTest {

    @BeforeEach
    void init() {
        final PlayerDao playerDao = new PlayerDaoImpl();
        playerDao.deleteAll();
    }

    @Test
    void save() {
        final PlayerDao playerDao = new PlayerDaoImpl();
        playerDao.save(Color.BLACK);
    }

    @Test
    void findAll() {
        final PlayerDao playerDao = new PlayerDaoImpl();
        playerDao.save(Color.BLACK);
        final Player player = playerDao.getPlayer();

        assertThat(player).isEqualTo(Player.of(Color.BLACK));
    }

    @Test
    void deleteAll() {
        final PlayerDao playerDao = new PlayerDaoImpl();
        playerDao.deleteAll();
        final Player player = playerDao.getPlayer();

        assertThat(player).isNull();
    }
}
