package chess.dao.jdbctemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import chess.domain.chessboard.ChessBoard;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.Symbol;
import chess.domain.piece.generator.NormalPiecesGenerator;
import chess.domain.position.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@TestPropertySource("classpath:application-test.properties")
public class BoardDaoTest {

    @Autowired
    private BoardDao boardDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE board IF EXISTS");
        jdbcTemplate.execute("DROP TABLE game IF EXISTS");
        jdbcTemplate.execute("create table game\n"
                + "(\n"
                + "    id    int auto_increment\n"
                + "        primary key,\n"
                + "    state varchar(20) not null\n"
                + ");");
        jdbcTemplate.execute("create table board\n"
                + "(\n"
                + "    position varchar(5)  not null,\n"
                + "    symbol   varchar(10) not null,\n"
                + "    color    varchar(10) not null,\n"
                + "    game_id  int         not null,\n"
                + "    primary key (position, game_id),\n"
                + "    constraint board_game_id_fk\n"
                + "        foreign key (game_id) references game (id)\n"
                + "            on update cascade on delete cascade\n"
                + ");");
        jdbcTemplate.update("insert into game(state) values (?)", "WhiteRunning");
    }

    @Test
    @DisplayName("체스 보드를 생성할 수 있다.")
    void save() {
        ChessBoard chessBoard = new ChessBoard(new NormalPiecesGenerator());
        assertThatCode(() ->
                boardDao.save(chessBoard, 1L)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("체스 보드를 조회할 수 있다")
    void find() {
        boardDao.save(new ChessBoard(new NormalPiecesGenerator()), 1L);

        assertThat(boardDao.find()).isInstanceOf(ChessBoard.class);
    }

    @Test
    @DisplayName("체스 보드를 갱신할 수 있다.")
    void update() {
        boardDao.save(new ChessBoard(new NormalPiecesGenerator()), 1L);
        Position position = Position.of("a2");
        Piece piece = Piece.of(Color.WHITE, Symbol.PAWN);

        assertThatCode(() ->
                boardDao.update(position, piece, 1L)
        ).doesNotThrowAnyException();
    }
}
