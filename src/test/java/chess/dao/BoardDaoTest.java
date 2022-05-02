package chess.dao;

import chess.domain.MovingPosition;
import chess.domain.Position;
import chess.dto.MovingPositionDto;
import chess.piece.Color;
import chess.piece.King;
import chess.piece.Piece;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BoardDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BoardDao dao;

    @AfterEach()
    void clear() {
        jdbcTemplate.execute("DELETE FROM game");
    }

    @Test
    @DisplayName("올바른 체스보드를 불러오는지 확인")
    void findByGameId() {
        int id = insertAndGetId();
        jdbcTemplate.update("INSERT INTO board(game_id,piece_id,position_id) values (?,1,1)", id);

        Map<Position, Piece> chessboard = dao.findByGameId(id).getBoard();

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
        int id = insertAndGetId();
        Map<Position, Piece> chessboard = Map.of(new Position(0, 0), new King(Color.WHITE));

        dao.saveBoard(id, chessboard);

        assertThat(jdbcTemplate.queryForObject("SELECT position_id FROM board WHERE game_id=?", Integer.class, id)).isEqualTo(1);
        assertThat(jdbcTemplate.queryForObject("SELECT piece_id FROM board WHERE game_id=?", Integer.class, id)).isEqualTo(1);
    }

    @Test
    @DisplayName("움직인 경우 올바르게 저장하는지 확인")
    void saveMove() {
        int id = insertAndGetId();
        jdbcTemplate.update("INSERT INTO board(game_id,position_id,piece_id) VALUES (?,1,1)", id);
        jdbcTemplate.update("INSERT INTO board(game_id,position_id,piece_id) VALUES (?,2,2)", id);

        dao.saveMove(id, new MovingPositionDto(new MovingPosition("a8", "b8")));

        assertThat(jdbcTemplate.queryForObject("SELECT piece_id FROM board WHERE game_id=? AND position_id=1", Integer.class, id)).isEqualTo(13);
        assertThat(jdbcTemplate.queryForObject("SELECT piece_id FROM board WHERE game_id=? AND position_id=2", Integer.class, id)).isEqualTo(1);
    }

    private int insertAndGetId() {
        final String sql = "INSERT INTO game(name,password) VALUES ('name','password')";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

}
