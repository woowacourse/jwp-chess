package chess.dao;

import chess.dao.dto.GameDto;
import chess.domain.position.Position;
import chess.dto.PieceDto;
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
        Long id = gameDao.save(new GameDto("라라", "1234", "white", "playing"));
        PieceDto pieceDto = new PieceDto("a2", "white", "pawn", id);
        pieceDao.save(pieceDto);

        // when
        pieceDao.removeByPosition(id, Position.of("a2"));

        // then
        assertThat(getPieceCount(id)).isEqualTo(0);
    }

    @Test
    @DisplayName("전체 기물 정보 삭제")
    void removeAll() {
        // given
        Long id = gameDao.save(new GameDto("라라", "1234", "white", "playing"));
        PieceDto pieceDtoA2 = new PieceDto("a2", "white", "pawn", id);
        PieceDto pieceDtoA3 = new PieceDto("a3", "white", "pawn", id);
        pieceDao.save(pieceDtoA2);
        pieceDao.save(pieceDtoA3);

        // when
        pieceDao.removeAll();

        // then
        assertThat(getPieceCount(id)).isEqualTo(0);
    }

    @Test
    @DisplayName("전체 기물 정보 저장")
    void saveAll() {
        // given
        Long id = gameDao.save(new GameDto("라라", "1234", "white", "playing"));
        PieceDto pieceDtoA2 = new PieceDto("a2", "white", "pawn", id);
        PieceDto pieceDtoA3 = new PieceDto("a3", "white", "pawn", id);

        // when
        pieceDao.saveAll(List.of(pieceDtoA2, pieceDtoA3));

        // then
        assertThat(getPieceCount(id)).isEqualTo(2);
    }

    @Test
    @DisplayName("기물 정보 저장")
    void save() {
        // given
        Long id = gameDao.save(new GameDto("라라", "1234", "white", "playing"));
        PieceDto pieceDto = new PieceDto("a2", "white", "pawn", id);

        // when
        pieceDao.save(pieceDto);

        // then
        assertThat(getPieceCount(id)).isEqualTo(1);
    }

    @Test
    @DisplayName("전체 기물 정보 조회")
    void findAll() {
        // given
        Long id = gameDao.save(new GameDto("라라", "1234", "white", "playing"));
        PieceDto pieceDtoA2 = new PieceDto("a2", "white", "pawn", id);
        PieceDto pieceDtoA3 = new PieceDto("a3", "white", "pawn", id);
        pieceDao.saveAll(List.of(pieceDtoA2, pieceDtoA3));

        // when
        List<PieceDto> pieceDtos = pieceDao.findPiecesByGameId(id);

        // then
        assertAll(
                () -> assertThat(pieceDtos.size()).isEqualTo(2),
                () -> assertThat(pieceDtos).containsOnly(pieceDtoA2, pieceDtoA3)
        );
    }

    @Test
    @DisplayName("기물 정보 수정")
    void update() {
        // given
        Long id = gameDao.save(new GameDto("라라", "1234", "white", "playing"));
        PieceDto pieceDtoA2 = new PieceDto("a2", "white", "pawn", id);
        PieceDto pieceDtoA3 = new PieceDto("a3", "white", "pawn", id);
        pieceDao.saveAll(List.of(pieceDtoA2, pieceDtoA3));

        // when
        pieceDao.updatePosition(id, Position.of("a2"), Position.of("a5"));

        // then
        assertThatCode(
                () -> jdbcTemplate.queryForObject(
                        "select * from piece where position = 'a5'",
                        (resultSet, rowNum) ->
                                new PieceDto(
                                        resultSet.getString("position"),
                                        resultSet.getString("color"),
                                        resultSet.getString("type")
                                )
                )
        ).doesNotThrowAnyException();
    }

    private Integer getPieceCount(Long id) {
        return jdbcTemplate.queryForObject("select count(*) from piece where game_id = " + id, Integer.class);
    }
}
