package chess.dao;

import chess.dao.dto.ChessGame;
import chess.domain.board.Square;
import chess.domain.manager.ChessGameManager;
import chess.domain.manager.ChessGameManagerFactory;
import chess.domain.piece.Pawn;
import chess.domain.position.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

import static chess.domain.piece.attribute.Color.WHITE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
class JdbcTemplateChessDaoTest {
    private static final long DEFAULT_CHESS_GAME_ID = 1;
    ChessGame chessGame;
    ChessGameManager sampleGame;

    private final JdbcTemplate jdbcTemplate;
    private final JdbcTemplateChessDao jdbcTemplateChessDao;

    @Autowired
    public JdbcTemplateChessDaoTest(JdbcTemplate jdbcTemplate, @Qualifier("dataSource") DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplateChessDao = new JdbcTemplateChessDao(jdbcTemplate, dataSource);
    }

    @BeforeEach
    void beforeEach() {
        String sample = "RKBQKBKRPPPPPPPP................................pppppppprkbqkbkr"; // move a2 a3 한 번 진행
        chessGame = new ChessGame(DEFAULT_CHESS_GAME_ID, WHITE, true, sample);
        sampleGame = ChessGameManagerFactory.loadingGame(chessGame);
    }

    @Test
    @DisplayName("체스 게임을 저장한다.")
    void save() {
        long newId = jdbcTemplateChessDao.save(chessGame);
        assertThat(newId).isEqualTo(2);
    }

    @Test
    @DisplayName("id로 체스 게임을 찾는다.")
    void findById() {
        assertThat(jdbcTemplateChessDao.findById(DEFAULT_CHESS_GAME_ID).isPresent()).isTrue();
    }

    @Test
    @DisplayName("체스 게임 정보를 업데이트한다.")
    void update() {
        sampleGame.move(Position.of("a2"), Position.of("a4"));

        jdbcTemplateChessDao.update(new ChessGame(sampleGame));

        ChessGame expectedChessGame = jdbcTemplateChessDao.findById(DEFAULT_CHESS_GAME_ID).get();
        ChessGameManager expectedChessGameManager = ChessGameManagerFactory.loadingGame(expectedChessGame);
        Square a4 = expectedChessGameManager.getBoard().findByPosition(Position.of("a4"));
        assertThat(a4.getPiece().getClass()).isEqualTo(Pawn.class);
        assertThat(a4.getPiece().getColor()).isEqualTo(WHITE);
    }

    @Test
    void findAllOnRunning() {
        List<ChessGame> allOnRunning = jdbcTemplateChessDao.findAllOnRunning();

        assertThat(allOnRunning.size()).isEqualTo(1);
    }

    @Test
    void delete() {
        //when
        jdbcTemplateChessDao.delete(DEFAULT_CHESS_GAME_ID);

        //then
        assertThatThrownBy(() -> jdbcTemplateChessDao.findById(DEFAULT_CHESS_GAME_ID))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}