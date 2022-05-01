package chess.dao;

import chess.domain.MovingPosition;
import chess.domain.Position;
import chess.dto.MovingPositionDto;
import chess.piece.Color;
import chess.piece.King;
import chess.piece.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BoardDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private BoardDao dao;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM game");
        jdbcTemplate.execute("ALTER TABLE game AUTO_INCREMENT = 1");
    }

    @Test
    @DisplayName("올바른 체스보드를 불러오는지 확인")
    void findByGameId() {
        jdbcTemplate.execute("INSERT INTO game(name,password) VALUES ('name','password')");
        jdbcTemplate.execute("INSERT INTO board(game_id,piece_id,position_id) values (1,1,1)");

        Map<Position, Piece> chessboard = dao.findByGameId(1).getBoard();

        for (Position position : chessboard.keySet()) {
            assertThat(position.getX()).isEqualTo(0);
            assertThat(position.getY()).isEqualTo(0);

            Piece piece = chessboard.get(position);
            assertThat(piece.getTypeToString()).isEqualTo("k");
            assertThat(piece.getColorToString()).isEqualTo("WHITE");
        }
    }

    @Test
    @DisplayName("올바르게 저장하는지 확인")
    void saveBoard() {
        jdbcTemplate.execute("INSERT INTO game(name,password) VALUES ('name','password')");
        Map<Position, Piece> chessboard = Map.of(new Position(0, 0), new King(Color.WHITE));

        dao.saveBoard(1, chessboard);

        assertThat(jdbcTemplate.queryForObject("SELECT position_id FROM board WHERE game_id=1", Long.class)).isEqualTo(1);
        assertThat(jdbcTemplate.queryForObject("SELECT piece_id FROM board WHERE game_id=1", Long.class)).isEqualTo(1);
    }

    @Test
    @DisplayName("움직인 경우 올바르게 저장하는지 확인")
    void saveMove() {
        jdbcTemplate.execute("INSERT INTO game(name,password) VALUES ('name','password')");
        jdbcTemplate.execute("INSERT INTO board(game_id,position_id,piece_id) VALUES (1,1,1)");
        jdbcTemplate.execute("INSERT INTO board(game_id,position_id,piece_id) VALUES (1,2,2)");

        dao.saveMove(1, new MovingPositionDto(new MovingPosition("a8", "b8")));

        assertThat(jdbcTemplate.queryForObject("SELECT piece_id FROM board WHERE game_id=1 AND position_id=1",Long.class)).isEqualTo(13);
        assertThat(jdbcTemplate.queryForObject("SELECT piece_id FROM board WHERE game_id=1 AND position_id=2",Long.class)).isEqualTo(1);
    }

}
