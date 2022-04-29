package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.auth.EncryptedAuthCredentials;
import chess.entity.FullGameEntity;
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

        List<GameEntity> expected = findAllTestData();

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

    @DisplayName("findFullDataById 메서드로 id에 해당되는 모든 데이터 조회가능")
    @Nested
    class FindFullDataByIdTest {

        @Test
        void 존재하는_게임_조회가능() {
            FullGameEntity actual = dao.findFullDataById(1);

            FullGameEntity expected = new FullGameEntity(1, "진행중인_게임", "encrypted1", "enemy1", true);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 게임이_존재하지_않는_경우_예외발생() {
            assertThatThrownBy(() -> dao.findFullDataById(99999))
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

        assertThat(actual).isEqualTo(5);
    }

    @Test
    void countRunningGames_메서드로_running값이_참인_데이터의_개수_조회가능() {
        int actual = dao.countRunningGames();

        assertThat(actual).isEqualTo(4);
    }

    @DisplayName("saveAndGetGeneratedId 메서드는 게임 저장 후 id값 반환")
    @Nested
    class SaveAndGetGeneratedIdTest {

        @Test
        void 저장_성공시_저장한_데이터의_id값_반환() {
            int actual = dao.saveAndGetGeneratedId(
                    new EncryptedAuthCredentials("name", "passwordHash"));

            int previousDataCount = findAllTestData().size();

            assertThat(actual).isGreaterThan(previousDataCount);
        }

        @Test
        void 중복된_이름으로_게임_저장시도시_예외발생() {
            EncryptedAuthCredentials invalidAuthInfo = new EncryptedAuthCredentials("이미_존재하는_게임명", "asd");

            assertThatThrownBy(() -> dao.saveAndGetGeneratedId(invalidAuthInfo))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("게임을 생성하는데 실패하였습니다.");
        }
    }

    @DisplayName("saveOpponent 메서드는 상대방 플레이어의 정보를 저장")
    @Nested
    class SaveOpponentTest {

        @Test
        void 방명에_대응되는_게임에_아직_상대방_플레이어가_없으면_저장_성공() {
            dao.saveOpponent(new EncryptedAuthCredentials("참여자가_없는_게임", "비밀번호"));

            String actual = jdbcTemplate.queryForObject(
                    "SELECT opponent_password FROM game WHERE name = '참여자가_없는_게임'", String.class);

            assertThat(actual).isEqualTo("비밀번호");
        }

        @Test
        void 방주인의_비밀번호와_중복된_비밀번호_입력시_예외발생() {
            EncryptedAuthCredentials invalidAuthInfo = new EncryptedAuthCredentials("참여자가_없는_게임", "encrypted5");

            assertThatThrownBy(() -> dao.saveOpponent(invalidAuthInfo))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상대방 플레이어 저장에 실패하였습니다.");
        }

        @Test
        void 이미_상대방_플레이어가_존재하는_경우_예외발생() {
            EncryptedAuthCredentials invalidAuthInfo = new EncryptedAuthCredentials("참여자가_있는_게임", "비밀번호");

            assertThatThrownBy(() -> dao.saveOpponent(invalidAuthInfo))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상대방 플레이어 저장에 실패하였습니다.");
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

    private List<GameEntity> findAllTestData() {
        return List.of(
                new GameEntity(1, "진행중인_게임", true),
                new GameEntity(2, "종료된_게임", false),
                new GameEntity(3, "이미_존재하는_게임명", true),
                new GameEntity(4, "참여자가_있는_게임", true),
                new GameEntity(5, "참여자가_없는_게임", true));
    }
}
