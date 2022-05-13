package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.board.BoardInitializer;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.entity.PieceEntity;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

@SpringBootTest
class BoardDaoTest {
    @Autowired
    BoardDao boardDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void after() {
        jdbcTemplate.update("delete from PIECE");
        jdbcTemplate.update("delete from GAME");
    }

    @DisplayName("새로운 게임 보드를 저장한다.")
    @Test
    void saveAll() {
        // given
        Long gameId = saveGame();
        Map<Position, Piece> board = BoardInitializer.get().getSquares();

        // when
        boardDao.saveAll(gameId, board);

        // then
        List<PieceEntity> result = boardDao.findAllByGameId(gameId);
        assertThat(result).hasSize(64);
    }

    @DisplayName("DB에 초기 보드를 저장한 후 조회하면 a1 위치에 흰색 룩이 있다.")
    @Test
    void findAllByGameId() {
        // given
        Long gameId = saveGame();
        boardDao.saveAll(gameId, BoardInitializer.get().getSquares());

        // when
        List<PieceEntity> pieces = boardDao.findAllByGameId(gameId);

        // then
        PieceEntity pieceA1 = pieces.stream()
                .filter(pieceEntity -> pieceEntity.getPosition().equals("a1"))
                .findAny()
                .get();
        assertThat(pieceA1)
                .extracting("gameId", "type", "white")
                .containsExactly(gameId, "rook", true);
    }

    @DisplayName("gameId에 해당하는 piece를 모두 삭제한다.")
    @Test
    void deleteAllByGameId() {
        // given
        Long gameId = saveGame();
        boardDao.saveAll(gameId, BoardInitializer.get().getSquares());

        // when
        boardDao.deleteAllByGameId(gameId);

        // then
        assertThat(boardDao.findAllByGameId(gameId)).isEmpty();
    }

    private Long saveGame() {
        final String sql = "insert into game (title, password, finished, white_turn) values ('test', 'pwd', false, true)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> connection.prepareStatement(sql, new String[]{"id"}), keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}
