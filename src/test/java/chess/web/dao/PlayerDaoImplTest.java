package chess.web.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import chess.domain.game.state.Player;
import chess.domain.piece.Piece;
import chess.domain.piece.StartedPawn;
import chess.domain.piece.position.Position;
import chess.domain.piece.property.Color;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class PlayerDaoImplTest {
    private PlayerDao playerDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        playerDao = new PlayerDaoImpl(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE player IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE player(" +
                "color VARCHAR(5))");
    }

    @Test
    void save() {
        playerDao.save(Color.WHITE);
        Player player = playerDao.getPlayer();
        assertThat(player.name()).isEqualTo(Color.WHITE.name());
    }

    @Test
    void deleteAll() {
        playerDao.deleteAll();
        assertThatThrownBy(() -> playerDao.getPlayer())
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void getPlayer() {
        playerDao.save(Color.WHITE);
        Player player = playerDao.getPlayer();
        assertThat(player).isNotNull();
    }
}
