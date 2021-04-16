package chess.dao.jdbctemplate;

import chess.controller.web.dto.state.StateResponseDto;
import chess.dao.GameDao;
import chess.dao.StateDao;
import chess.domain.board.Board;
import chess.domain.board.BoardInitializer;
import chess.domain.game.Game;
import chess.domain.manager.ChessManager;
import chess.domain.piece.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@DataJdbcTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@TestPropertySource("classpath:application-test.properties")
class StateDaoTemplateTest {

    private JdbcTemplate jdbcTemplate;
    private StateDao stateDao;
    private GameDao gameDao;
    private Long newGameId;

    @Autowired
    public StateDaoTemplateTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.stateDao = new StateDaoTemplate(jdbcTemplate);
        this.gameDao = new GameDaoTemplate(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        newGameId = gameDao.saveGame(Game.of("게임", "흰색유저", "흑색유저"));
    }

    @Test
    void saveState() {
        //given
        Board board = BoardInitializer.initiateBoard();
        Owner turnOwner = Owner.WHITE;
        int turnNumber = 1;
        boolean isPlaying = true;
        ChessManager chessManager = new ChessManager(board, turnOwner, turnNumber, isPlaying);

        //when
        stateDao.saveState(chessManager, newGameId);
        StateResponseDto findState = stateDao.findStateByGameId(newGameId);

        //then
        assertThat(findState).isNotNull();
        assertThat(findState.getTurnOwner()).isEqualTo(turnOwner.name());
        assertThat(findState.getTurnNumber()).isEqualTo(turnNumber);
        assertThat(findState.isPlaying()).isTrue();
    }

    @Test
    void updateState() {
        //given
        Board board = BoardInitializer.initiateBoard();
        ChessManager chessManager = new ChessManager(board);
        stateDao.saveState(chessManager, newGameId);
        Owner updatedTurnOwner = Owner.BLACK;
        int updatedTurnNumber = 2;
        boolean updatedIsPlaying = false;
        ChessManager updatedChessManager = new ChessManager(board, updatedTurnOwner, updatedTurnNumber, updatedIsPlaying);

        //when
        stateDao.updateState(updatedChessManager, newGameId);
        StateResponseDto findUpdatedState = stateDao.findStateByGameId(newGameId);

        //then
        assertThat(findUpdatedState).isNotNull();
        assertThat(findUpdatedState.getTurnOwner()).isEqualTo(updatedTurnOwner.name());
        assertThat(findUpdatedState.getTurnNumber()).isEqualTo(2);
        assertThat(findUpdatedState.isPlaying()).isFalse();
    }

    @Test
    void findStateByGameId() {
        //given
        Board board = BoardInitializer.initiateBoard();
        Owner turnOwner = Owner.BLACK;
        int turnNumber = 44;
        boolean isPlaying = false;
        ChessManager chessManager = new ChessManager(board, turnOwner, turnNumber, isPlaying);
        stateDao.saveState(chessManager, newGameId);

        //when
        StateResponseDto findState = stateDao.findStateByGameId(newGameId);

        //then
        assertThat(findState).isNotNull();
        assertThat(findState.getTurnOwner()).isEqualTo(turnOwner.name());
        assertThat(findState.getTurnNumber()).isEqualTo(turnNumber);
        assertThat(findState.isPlaying()).isFalse();
    }
}