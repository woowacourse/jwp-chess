package chess.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

import chess.model.dto.GameResultDto;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ChessResultDaoTest {

    private static final ChessResultDao CHESS_RESULT_DAO = ChessResultDao.getInstance();
    private static final String FIRST_NAME = "TEST16090102";
    private static final String SECOND_NAME = "TEST26090102";
    private static final Set<String> USER_NAMES;

    static {
        Set<String> userNames = new HashSet<>();
        userNames.add(FIRST_NAME);
        userNames.add(SECOND_NAME);
        USER_NAMES = Collections.unmodifiableSet(userNames);
    }

    @BeforeEach
    void setUp() {
        CHESS_RESULT_DAO.createUserNames(new HashSet<>(Arrays.asList(FIRST_NAME, SECOND_NAME)));
    }

    @AfterEach
    void tearDown() {
        CHESS_RESULT_DAO.delete(USER_NAMES);
    }

    @Test
    void getInstance() {
        ChessResultDao chessResultDao1 = ChessResultDao.getInstance();
        ChessResultDao chessResultDao2 = ChessResultDao.getInstance();
        assertThat(chessResultDao1 == chessResultDao2).isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {"1, 0, 0", "0, 1, 0", "0, 0, 1"})
    void update(int winCount, int drawCount, int loseCount) {
        GameResultDto gameResultDto = new GameResultDto(1, 0, 0);
        CHESS_RESULT_DAO.update(FIRST_NAME, gameResultDto);
        assertThat(CHESS_RESULT_DAO.findWinOrDraw(FIRST_NAME).orElseThrow(IllegalAccessError::new))
            .isEqualTo(gameResultDto);
    }

    @Test
    void insert() {
        CHESS_RESULT_DAO.delete(USER_NAMES);
        assertThat(CHESS_RESULT_DAO.findWinOrDraw(FIRST_NAME)).isEmpty();
        assertThat(CHESS_RESULT_DAO.findWinOrDraw(SECOND_NAME)).isEmpty();

        CHESS_RESULT_DAO.createUserNames(new HashSet<>(Arrays.asList(FIRST_NAME, SECOND_NAME)));
        assertThat(CHESS_RESULT_DAO.findWinOrDraw(FIRST_NAME)).isNotEmpty();
        assertThat(CHESS_RESULT_DAO.findWinOrDraw(SECOND_NAME)).isNotEmpty();
    }

    @Test
    void delete() {
        assertThat(CHESS_RESULT_DAO.findWinOrDraw(FIRST_NAME)).isNotEmpty();
        assertThat(CHESS_RESULT_DAO.findWinOrDraw(SECOND_NAME)).isNotEmpty();

        CHESS_RESULT_DAO.delete(USER_NAMES);
        assertThat(CHESS_RESULT_DAO.findWinOrDraw(FIRST_NAME)).isEmpty();
        assertThat(CHESS_RESULT_DAO.findWinOrDraw(SECOND_NAME)).isEmpty();
    }

    @Test
    void select() {
        assertThat(CHESS_RESULT_DAO.findWinOrDraw(FIRST_NAME)).isNotEmpty();
        assertThat(CHESS_RESULT_DAO.findWinOrDraw(SECOND_NAME)).isNotEmpty();
    }

    @Test
    void getWinOrDraw() {
        Optional<GameResultDto> gameResult = CHESS_RESULT_DAO.findWinOrDraw(FIRST_NAME);
        assertThat(gameResult.isPresent()).isTrue();
        GameResultDto gameResultDto = gameResult.orElseThrow(IllegalArgumentException::new);
        assertThat(gameResultDto.getWinCount()).isGreaterThanOrEqualTo(0);
        assertThat(gameResultDto.getDrawCount()).isGreaterThanOrEqualTo(0);
        assertThat(gameResultDto.getLoseCount()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void getUsers() {
        assertThat(CHESS_RESULT_DAO.findUsers().contains(FIRST_NAME)).isTrue();
        assertThat(CHESS_RESULT_DAO.findUsers().contains(SECOND_NAME)).isTrue();
    }
}