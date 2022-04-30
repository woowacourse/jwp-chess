package chess.dao;

import static chess.domain.CachedPosition.a2;
import static chess.domain.CachedPosition.a3;
import static chess.domain.CachedPosition.b2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import chess.controller.dto.response.PieceResponse;
import chess.dao.entity.GameEntity;
import chess.domain.GameState;
import chess.domain.piece.Color;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
public class PieceDaoTest {

    private static final String NOT_HAVE_DATA = "해당하는 기물이 없습니다.";

    @Autowired
    private DataSource dataSource;

    private GameDao gameDao;
    private PieceDao pieceDao;

    private Long gameId;

    @BeforeEach
    void setUp() {
        gameDao = new GameDao(dataSource);
        pieceDao = new PieceDao(dataSource);

        GameEntity gameEntity = new GameEntity(null, "game", "password", "salt", GameState.READY);
        gameId = gameDao.save(gameEntity);
    }

    @DisplayName("기물 저장 테스트")
    @Test
    void save() {
        pieceDao.save(gameId, a2, new Pawn(Color.WHITE));
    }

    @DisplayName("게임의 전체 기물 조회 테스트")
    @Test
    void findAll() {
        pieceDao.save(gameId, a2, new Pawn(Color.WHITE));
        pieceDao.save(gameId, b2, new Pawn(Color.WHITE));

        List<PieceResponse> pieces = pieceDao.findAll(gameId);

        assertThat(pieces.size()).isEqualTo(2);
    }

    @DisplayName("위치에 맞는 기물 조회 테스트")
    @Test
    void find() {
        pieceDao.save(gameId, a2, new Pawn(Color.WHITE));

        Optional<Piece> maybePiece = pieceDao.find(gameId, a2);
        Piece actual = maybePiece.orElseGet(() -> fail(NOT_HAVE_DATA));

        assertThat(actual).isEqualTo(new Pawn(Color.WHITE));
    }

    @DisplayName("기물의 위치 변경 테스트")
    @Test
    void update() {
        pieceDao.save(gameId, a2, new Pawn(Color.WHITE));

        pieceDao.updatePosition(gameId, a2, a3);
        Optional<Piece> shouldEmpty = pieceDao.find(gameId, a2);
        Piece actual = pieceDao.find(gameId, a3).orElseGet(() -> fail(NOT_HAVE_DATA));

        Assertions.assertAll(
                () -> assertThat(shouldEmpty.isPresent()).isFalse(),
                () -> assertThat(actual).isEqualTo(new Pawn(Color.WHITE))
        );
    }

    @DisplayName("위치에 맞는 기물 삭제 테스트")
    @Test
    void delete() {
        pieceDao.save(gameId, a2, new Pawn(Color.WHITE));

        pieceDao.deleteByGameIdAndPosition(gameId, a2);
        Optional<Piece> maybePiece = pieceDao.find(gameId, a2);

        assertThat(maybePiece.isPresent()).isFalse();
    }

    @DisplayName("게임을 삭제하면 전체 기물이 삭제 되는지 테스트")
    @Test
    void deleteAll() {
        gameDao.delete(gameId);
        List<PieceResponse> pieces = pieceDao.findAll(gameId);

        assertThat(pieces).isEmpty();
    }
}
