package chess.dao2;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.auth.EncryptedAuthCredentials;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class GameDao2Test {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GameDao2 dao;

    @Test
    void saveAndGetGeneratedId_메서드는_저장한_데이터의_id값_반환() {
        int actual = dao.saveAndGetGeneratedId(
                new EncryptedAuthCredentials("name", "passwordHash"));

        assertThat(actual).isEqualTo(4);
    }

    @Test
    void finishGame_메서드로_게임을_종료된_상태로_변경가능() {
        dao.finishGame(1);

        boolean actual = jdbcTemplate.queryForObject(
                "SELECT running FROM game2 WHERE id = 1", Boolean.class);

        assertThat(actual).isFalse();
    }

    @Test
    void checkById_메서드로_id에_해당되는_데이터_존재여부_확인가능() {
        boolean actual = dao.checkById(1);

        assertThat(actual).isTrue();
    }

    @Test
    void checkById_메서드에서_id에_해당되는_데이터가_없으면_거짓_반환() {
        boolean actual = dao.checkById(9999);

        assertThat(actual).isFalse();
    }

    @Test
    void countAll_메서드로_여태까지_저장된_모든_데이터의_개수_조회가능() {
        int actual = dao.countAll();

        assertThat(actual).isEqualTo(3);
    }

    @Test
    void countRunningGames_메서드로_running값이_참인_데이터의_개수_조회가능() {
        int actual = dao.countRunningGames();

        assertThat(actual).isEqualTo(2);
    }
}
