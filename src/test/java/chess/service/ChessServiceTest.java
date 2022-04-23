package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.PieceDao;
import chess.dao.PieceDaoJdbc;
import chess.dao.TurnDao;
import chess.dao.TurnDaoJdbc;
import chess.dto.MoveDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class ChessServiceTest {

    private ChessService chessService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        PieceDao pieceDao = new PieceDaoJdbc(jdbcTemplate);
        TurnDao turnDao = new TurnDaoJdbc(jdbcTemplate);
        this.chessService = new ChessService(pieceDao, turnDao);

        jdbcTemplate.execute("drop table piece if exists");
        jdbcTemplate.execute("CREATE TABLE piece (\n" +
                "    position varchar(3) not null primary key,\n" +
                "    name varchar(2) not null,\n" +
                "    team varchar(5) not null)");
        jdbcTemplate.execute("drop table turn if exists");
        jdbcTemplate.execute("CREATE TABLE turn (team varchar(5) not null primary key)");
        jdbcTemplate.execute("insert into turn (team) values ('WHITE')");

        chessService.initializeGame();
    }

    @Test
    void initializeGame() {
        assertThat(chessService.initializeGame()).isNotNull();
    }

    @Test
    void load() {
        assertThat(chessService.load()).isNotNull();
    }

    @Test
    void move() {
        assertThat(chessService.move(new MoveDto("a2", "a3"))).isNotNull();
    }

    @Test
    void getStatus() {
        assertThat(chessService.getStatus().getScoreStatus()).isEqualTo("WHITE: 38.0\n" + "BLACK: 38.0");
    }

    @Test
    void getResult() {
        assertThat(chessService.getResult().getResult()).isEqualTo("무승부입니다!");
    }
}