package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.Board;
import chess.domain.BoardInitializer;
import chess.domain.ChessGame;
import chess.domain.Member;
import chess.domain.Participant;
import chess.domain.Room;
import chess.domain.piece.detail.Team;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@Sql("classpath:init.sql")
class SpringGameDaoTest {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private GameDao gameDao;

    @BeforeEach
    void setup() {
        final Member one = new Member(1L, "one");
        final Member two = new Member(2L, "two");
        final Board board = new Board(BoardInitializer.create());
        final Participant participant = new Participant(one, two);
        final Room room = new Room("some", "123", participant);
        final ChessGame game = new ChessGame(1L, board, Team.WHITE, room);

        memberDao.save(one);
        memberDao.save(two);
        gameDao.save(game);
    }

    @Test
    @DisplayName("정상적으로 게임이 생성되는지 확인한다.")
    void saveGame() {
        final ChessGame actual = gameDao.findById(1L).get();

        assertThat(actual.getTurn().name()).isEqualTo("WHITE");
    }

    @Test
    @DisplayName("정상적으로 게임을 불러오는지 확인한다.")
    void findById() {
        final Optional<ChessGame> game = gameDao.findById(1L);

        assertAll(
                () -> assertThat(game).isNotNull(),
                () -> assertThat(game.get().getId()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("저장소에 저장된 모든 게임을 불러온다.")
    void findAll() {
        final Member one = new Member(1L, "one");
        final Member two = new Member(2L, "two");
        final Board board = new Board(BoardInitializer.create());
        final Participant participant = new Participant(one, two);
        final Room room = new Room("some", "123", participant);
        final ChessGame game = new ChessGame(2L, board, Team.WHITE, room);
        gameDao.save(game);

        assertThat(gameDao.findAll().size()).isEqualTo(2);
    }
}
