package chess.dao.jdbctemplate;

import chess.dao.GameDao;
import chess.dao.StateDao;
import chess.dao.dto.game.GameDto;
import chess.dao.dto.state.StateDto;
import chess.domain.board.piece.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@DataJdbcTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestPropertySource("classpath:application-test.properties")
class StateDaoTemplateTest {

    private JdbcTemplate jdbcTemplate;
    private StateDao stateDao;
    private GameDao gameDao;
    private Long newGameId;

    public StateDaoTemplateTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.stateDao = new StateDaoTemplate(jdbcTemplate);
        this.gameDao = new GameDaoTemplate(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        newGameId = gameDao.save(new GameDto("게임", "흰색유저", "흑색유저"));
    }

    @Test
    void saveState() {
        //given
        Owner turnOwner = Owner.WHITE;
        int turnNumber = 1;
        boolean isPlaying = true;
        StateDto stateDto = new StateDto(newGameId, turnOwner.name(), turnNumber, isPlaying);

        //when
        stateDao.save(stateDto);
        StateDto findState = stateDao.findByGameId(newGameId);

        //then
        assertThat(findState).isNotNull();
        assertThat(findState.getTurnOwner()).isEqualTo(turnOwner.name());
        assertThat(findState.getTurnNumber()).isEqualTo(turnNumber);
        assertThat(findState.isPlaying()).isTrue();
    }

    @Test
    void updateState() {
        //given
        Owner turnOwner = Owner.WHITE;
        int turnNumber = 1;
        boolean isPlaying = true;
        StateDto stateDto = new StateDto(newGameId, turnOwner.name(), turnNumber, isPlaying);
        stateDao.save(stateDto);
        Owner updatedTurnOwner = Owner.BLACK;
        int updatedTurnNumber = 2;
        boolean updatedIsPlaying = false;
        StateDto updateStateDto = new StateDto(newGameId, updatedTurnOwner.name(), updatedTurnNumber, updatedIsPlaying);

        //when
        stateDao.update(updateStateDto);
        StateDto findUpdatedState = stateDao.findByGameId(newGameId);

        //then
        assertThat(findUpdatedState).isNotNull();
        assertThat(findUpdatedState.getTurnOwner()).isEqualTo(updatedTurnOwner.name());
        assertThat(findUpdatedState.getTurnNumber()).isEqualTo(2);
        assertThat(findUpdatedState.isPlaying()).isFalse();
    }

    @Test
    void findStateByGameId() {
        //given
        Owner turnOwner = Owner.BLACK;
        int turnNumber = 44;
        boolean isPlaying = false;
        StateDto stateDto = new StateDto(newGameId, turnOwner.name(), turnNumber, isPlaying);
        stateDao.save(stateDto);

        //when
        StateDto findState = stateDao.findByGameId(newGameId);

        //then
        assertThat(findState).isNotNull();
        assertThat(findState.getTurnOwner()).isEqualTo(turnOwner.name());
        assertThat(findState.getTurnNumber()).isEqualTo(turnNumber);
        assertThat(findState.isPlaying()).isFalse();
    }
}