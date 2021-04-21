package chess.domain.command.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.command.Command;
import chess.domain.command.Move;
import chess.domain.game.Side;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.TestPropertySource;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
class MoveCommandDaoTest {

    private MoveCommandDao moveCommandDao;

    private Integer gameId1;
    private Integer gameId2;
    private final List<Move> moves = Arrays.asList(
            new Move("move a2 a3"),
            new Move("move a7 a6"));

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        moveCommandDao = new MoveCommandDao(jdbcTemplate);

        gameId1 = insertGame("Chess Game1");
        gameId2 = insertGame("Chess Game2");

        jdbcTemplate.update(
                "INSERT INTO move_command (game_id, source_position, target_position, turn) "
                        + "VALUES (?, ?, ? ,?), (?, ?, ? ,?)",
                gameId1, moves.get(0).getSource(), moves.get(0).getTarget(), Side.WHITE.name(),
                gameId1, moves.get(1).getSource(), moves.get(1).getTarget(), Side.BLACK.name());
    }

    private int insertGame(String gameName) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final String sql2 = "INSERT INTO chess_game (name) VALUES (?)";
        jdbcTemplate.update(con -> {
            PreparedStatement pstmt = con.prepareStatement(sql2, new String[]{"id"});
            pstmt.setString(1, gameName);
            return pstmt;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Test
    void addMoveCommand() {
        Move newMove = new Move("H2", "H3");
        newMove.setGameId(String.valueOf(gameId2));
        newMove.setSide(Side.WHITE);

        moveCommandDao.addMoveCommand(newMove);

        List<Move> moves = jdbcTemplate.query("SELECT * FROM move_command WHERE game_id = ?",
                (rs, rowNum) -> new Move(
                        rs.getString("source_position"),
                        rs.getString("target_position")), gameId2);

        assertThat(moves).size().isEqualTo(1);
        Move move = moves.get(0);
        assertThat(move.getSource()).isEqualTo("H2");
        assertThat(move.getTarget()).isEqualTo("H3");
    }

    @Test
    void findCommandsByGameId() {
        List<Command> commands = moveCommandDao.findCommandsByGameId(String.valueOf(gameId1));

        assertThat(commands).size().isEqualTo(2);
    }
}