package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import chess.domain.position.Position;
import chess.dto.PieceDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class PieceDaoImplTest {

    @Autowired
    private PieceDaoImpl pieceDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE piece IF EXISTS");
        jdbcTemplate.execute("create table piece\n"
                + "(\n"
                + "    position varchar(5)  not null,\n"
                + "    type     varchar(10) not null,\n"
                + "    color    varchar(20) not null,\n"
                + "    primary key (position)\n"
                + ");");
    }

    @Test
    @DisplayName("기물 정보 삭제")
    void remove() {
        PieceDto pieceDto = PieceDto.of("a2", "white", "pawn");
        pieceDao.save(pieceDto);

        pieceDao.removeByPosition(Position.of("a2"));

        assertThat(getPieceCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("전체 기물 정보 삭제")
    void removeAll() {
        PieceDto pieceDtoA2 = PieceDto.of("a2", "white", "pawn");
        PieceDto pieceDtoA3 = PieceDto.of("a3", "white", "pawn");
        pieceDao.save(pieceDtoA2);
        pieceDao.save(pieceDtoA3);

        pieceDao.removeAll();

        assertThat(getPieceCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("전체 기물 정보 저장")
    void saveAll() {
        PieceDto pieceDtoA2 = PieceDto.of("a2", "white", "pawn");
        PieceDto pieceDtoA3 = PieceDto.of("a3", "white", "pawn");

        pieceDao.saveAll(List.of(pieceDtoA2, pieceDtoA3));

        assertThat(getPieceCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("기물 정보 저장")
    void save() {
        PieceDto pieceDto = PieceDto.of("a2", "white", "pawn");
        pieceDao.save(pieceDto);

        assertThat(getPieceCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("전체 기물 정보 조회")
    void findAll() {
        PieceDto pieceDtoA2 = PieceDto.of("a2", "white", "pawn");
        PieceDto pieceDtoA3 = PieceDto.of("a3", "white", "pawn");
        pieceDao.saveAll(List.of(pieceDtoA2, pieceDtoA3));

        List<PieceDto> pieceDtos = pieceDao.findAll();

        assertThat(pieceDtos).containsOnly(pieceDtoA2, pieceDtoA3);
    }

    @Test
    @DisplayName("기물 정보 수정")
    void update() {
        PieceDto pieceDto = PieceDto.of("a2", "white", "pawn");
        pieceDao.save(pieceDto);

        pieceDao.updatePosition(Position.of("a2"), Position.of("a3"));

        assertThatCode(
                () -> jdbcTemplate.queryForObject(
                        "select * from piece where position = 'a3'",
                        (resultSet, rowNum) ->
                                new PieceDto(
                                        resultSet.getString("position"),
                                        resultSet.getString("color"),
                                        resultSet.getString("type")
                                )
                )
        ).doesNotThrowAnyException();
    }

    private Integer getPieceCount() {
        return jdbcTemplate.queryForObject("select count(*) from piece", Integer.class);
    }
}
