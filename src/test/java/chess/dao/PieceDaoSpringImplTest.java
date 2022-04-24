package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.position.Position;
import chess.dto.PieceDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class PieceDaoSpringImplTest {

    private PieceDaoSpringImpl pieceDaoSpring;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        pieceDaoSpring = new PieceDaoSpringImpl(jdbcTemplate);

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
        pieceDaoSpring.save(pieceDto);

        pieceDaoSpring.remove(Position.of("a2"));

        assertThat(jdbcTemplate.queryForObject("select count(*) from piece", Integer.class)).isEqualTo(0);
    }

    @Test
    @DisplayName("전체 기물 정보 삭제")
    void removeAll() {
        PieceDto pieceDtoA2 = PieceDto.of("a2", "white", "pawn");
        PieceDto pieceDtoA3 = PieceDto.of("a3", "white", "pawn");
        pieceDaoSpring.save(pieceDtoA2);
        pieceDaoSpring.save(pieceDtoA3);

        pieceDaoSpring.removeAll();

        assertThat(jdbcTemplate.queryForObject("select count(*) from piece", Integer.class)).isEqualTo(0);
    }

    @Test
    @DisplayName("전체 기물 정보 저장")
    void saveAll() {
        PieceDto pieceDtoA2 = PieceDto.of("a2", "white", "pawn");
        PieceDto pieceDtoA3 = PieceDto.of("a3", "white", "pawn");

        pieceDaoSpring.saveAll(List.of(pieceDtoA2, pieceDtoA3));

        assertThat(jdbcTemplate.queryForObject("select count(*) from piece", Integer.class)).isEqualTo(2);
    }

    @Test
    @DisplayName("기물 정보 저장")
    void save() {
        PieceDto pieceDto = PieceDto.of("a2", "white", "pawn");
        pieceDaoSpring.save(pieceDto);

        assertThat(jdbcTemplate.queryForObject("select count(*) from piece", Integer.class)).isEqualTo(1);
    }
}
