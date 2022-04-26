package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.Board;
import chess.domain.BoardInitializer;
import chess.domain.ChessGame;
import chess.domain.Member;
import chess.domain.Participant;
import chess.domain.piece.detail.Team;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class SpringGameDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private GameDao gameDao;


    @BeforeEach
    void setup() {
        jdbcTemplate.execute("drop table if exists member");
        jdbcTemplate.execute("drop table if exists piece");
        jdbcTemplate.execute("drop table if exists game");

        jdbcTemplate.execute("create table Member ("
                + "id bigint auto_increment primary key, "
                + "name varchar(10) not null);");

        jdbcTemplate.execute("create table Game ( "
                + "id bigint auto_increment primary key, "
                + "turn varchar(10) not null,"
                + "white_member_id bigint, "
                + "black_member_id bigint);");

        jdbcTemplate.execute("create table Piece ( "
                + "game_id bigint not null, "
                + "square_file varchar(2) not null, "
                + "square_rank varchar(2) not null, "
                + "team varchar(10), "
                + "piece_type varchar(10) not null, "
                + "constraint fk_game_id foreign key(game_id) references Game(id));");

        final Member one = new Member(1L, "one");
        final Member two = new Member(2L, "two");
        final Board board = new Board(BoardInitializer.create());
        final ChessGame game = new ChessGame(1L, board, Team.WHITE, new Participant(one, two));

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
        final ChessGame game = new ChessGame(2L, board, Team.WHITE, new Participant(one, two));
        gameDao.save(game);

        assertThat(gameDao.findAll().size()).isEqualTo(2);
    }
}
