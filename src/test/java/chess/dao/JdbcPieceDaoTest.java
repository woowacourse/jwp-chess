package chess.dao;

import chess.dao.entity.GameEntity;
import chess.dao.entity.PieceEntity;
import chess.domain.position.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Sql("classpath:init.sql")
public class JdbcPieceDaoTest {

    private JdbcPieceDao pieceDao;
    private JdbcGameDao gameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        pieceDao = new JdbcPieceDao(jdbcTemplate);
        gameDao = new JdbcGameDao(jdbcTemplate);
    }

    @Test
    @DisplayName("기물 정보 삭제")
    void remove() {
        // given
        Long id = gameDao.save(new GameEntity("라라", "1234", "white", "playing"));
        PieceEntity piece = new PieceEntity("a2", "white", "pawn", id);
        pieceDao.save(piece);

        // when
        pieceDao.removeByPosition(id, Position.of("a2"));

        // then
        assertThat(getPieceCount(id)).isEqualTo(0);
    }

    @Test
    @DisplayName("전체 기물 정보 삭제")
    void removeAll() {
        // given
        Long id = gameDao.save(new GameEntity("라라", "1234", "white", "playing"));
        PieceEntity pieceA2 = new PieceEntity("a2", "white", "pawn", id);
        PieceEntity pieceA3 = new PieceEntity("a3", "white", "pawn", id);
        pieceDao.save(pieceA2);
        pieceDao.save(pieceA3);

        // when
        pieceDao.removeAll();

        // then
        assertThat(getPieceCount(id)).isEqualTo(0);
    }

    @Test
    @DisplayName("전체 기물 정보 저장")
    void saveAll() {
        // given
        Long id = gameDao.save(new GameEntity("라라", "1234", "white", "playing"));
        PieceEntity pieceA2 = new PieceEntity("a2", "white", "pawn", id);
        PieceEntity pieceA3 = new PieceEntity("a3", "white", "pawn", id);

        // when
        pieceDao.saveAll(List.of(pieceA2, pieceA3));

        // then
        assertThat(getPieceCount(id)).isEqualTo(2);
    }

    @Test
    @DisplayName("기물 정보 저장")
    void save() {
        // given
        Long id = gameDao.save(new GameEntity("라라", "1234", "white", "playing"));
        PieceEntity piece = new PieceEntity("a2", "white", "pawn", id);

        // when
        pieceDao.save(piece);

        // then
        assertThat(getPieceCount(id)).isEqualTo(1);
    }

    @Test
    @DisplayName("전체 기물 정보 조회")
    void findAll() {
        // given
        Long id = gameDao.save(new GameEntity("라라", "1234", "white", "playing"));
        PieceEntity pieceA2 = new PieceEntity("a2", "white", "pawn", id);
        PieceEntity pieceA3 = new PieceEntity("a3", "white", "pawn", id);
        pieceDao.saveAll(List.of(pieceA2, pieceA3));

        // when
        List<PieceEntity> pieces = pieceDao.findPiecesByGameId(id);

        // then
        assertAll(
                () -> assertThat(pieces.size()).isEqualTo(2),
                () -> assertThat(pieces).containsOnly(pieceA2, pieceA3)
        );
    }

    @Test
    @DisplayName("기물 정보 수정")
    void update() {
        // given
        Long id = gameDao.save(new GameEntity("라라", "1234", "white", "playing"));
        PieceEntity pieceA2 = new PieceEntity("a2", "white", "pawn", id);
        PieceEntity pieceA3 = new PieceEntity("a3", "white", "pawn", id);
        pieceDao.saveAll(List.of(pieceA2, pieceA3));

        // when
        pieceDao.updatePosition(id, Position.of("a2"), Position.of("a5"));

        // then
        assertThatCode(
                () -> jdbcTemplate.queryForObject(
                        "select * from piece where position = 'a5'",
                        (resultSet, rowNum) ->
                                new PieceEntity(
                                        resultSet.getString("position"),
                                        resultSet.getString("color"),
                                        resultSet.getString("type"),
                                        resultSet.getLong("game_id")
                                )
                )
        ).doesNotThrowAnyException();
    }

    private Integer getPieceCount(Long id) {
        return jdbcTemplate.queryForObject("select count(*) from piece where game_id = " + id, Integer.class);
    }
}
