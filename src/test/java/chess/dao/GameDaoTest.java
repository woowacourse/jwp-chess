package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.auth.EncryptedAuthCredentials;
import chess.entity.GameEntity;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class GameDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GameDao dao;

    @Test
    void findAll_메서드로_존재하는_모든_게임_정보를_조회가능() {
        List<GameEntity> actual = dao.findAll();

        List<GameEntity> expected = List.of(
                new GameEntity(1, "진행중인_게임", true),
                new GameEntity(2, "종료된_게임", false),
                new GameEntity(3, "이미_존재하는_게임명", true));

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("findById 메서드로 id에 해당되는 데이터 조회가능")
    @Nested
    class FindByIdTest {

        @Test
        void 존재하는_게임_조회가능() {
            GameEntity actual = dao.findById(1);

            GameEntity expected = new GameEntity(1, "진행중인_게임", true);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 게임이_존재하지_않는_경우_예외발생() {
         assertThatThrownBy(() -> dao.findById(99999))
                 .isInstanceOf(IllegalArgumentException.class)
                 .hasMessage("존재하지 않는 게임입니다.");
        }
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

    @DisplayName("saveAndGetGeneratedId 메서드는 게임 저장 후 id값 반환")
    @Nested
    class SaveAndGetGeneratedIdTest {

        @Test
        void 저장_성공시_저장한_데이터의_id값_반환() {
            int actual = dao.saveAndGetGeneratedId(
                    new EncryptedAuthCredentials("name", "passwordHash"));

            assertThat(actual).isGreaterThan(3);
        }

        @Test
        void 중복된_이름으로_게임_저장시도시_예외발생() {
            EncryptedAuthCredentials invalidAuthInfo = new EncryptedAuthCredentials("이미_존재하는_게임명", "asd");

            assertThatThrownBy(() -> dao.saveAndGetGeneratedId(invalidAuthInfo))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("게임을 생성하는데 실패하였습니다.");
        }
    }

    @Test
    void finishGame_메서드로_게임을_종료된_상태로_변경가능() {
        dao.finishGame(1);

        boolean actual = jdbcTemplate.queryForObject(
                "SELECT running FROM game WHERE id = 1", Boolean.class);

        assertThat(actual).isFalse();
    }

    @DisplayName("deleteGame 메서드로 종료된 게임 삭제가능")
    @Nested
    class DeleteGameTest {

        @Test
        void 올바른_비밀번호로_게임_삭제가능() {
            dao.deleteGame(new EncryptedAuthCredentials("종료된_게임", "encrypted2"));

            boolean exists = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM game WHERE id = 2", Integer.class) > 0;

            assertThat(exists).isFalse();
        }

        @Test
        void 잘못된_비밀번호로_게임_삭제시도시_예외발생() {
            EncryptedAuthCredentials existingGameData = new EncryptedAuthCredentials("종료된_게임", "wrong_password");
            assertThatThrownBy(() -> dao.deleteGame(existingGameData))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("게임을 삭제하는 데 실패하였습니다!");
        }

        @Test
        void 아직_진행중인_게임_삭제시도시_예외발생() {
            EncryptedAuthCredentials existingGameData = new EncryptedAuthCredentials("진행중인_게임", "encrypted1");
            assertThatThrownBy(() -> dao.deleteGame(existingGameData))
                    .hasMessage("게임을 삭제하는 데 실패하였습니다!");
        }
    }
}
