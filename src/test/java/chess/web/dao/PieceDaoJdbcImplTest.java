package chess.web.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.Color;
import chess.domain.piece.King;
import chess.domain.piece.Queen;
import chess.domain.position.Position;
import chess.web.dto.PieceDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class PieceDaoJdbcImplTest {

    private PieceDao pieceDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        pieceDao = new PieceDaoJdbcImpl(jdbcTemplate);

        jdbcTemplate.execute("drop table piece if exists");
        jdbcTemplate.execute("create table piece ("
                + " id int not null auto_increment primary key,"
                + " piece_type varchar(1) not null,"
                + " position varchar(2) not null,"
                + " color varchar(5) not null)");

        pieceDao.save(new PieceDto(new King(Color.WHITE), new Position("a1")));
    }

    @DisplayName("기물을 전부 삭제한다.")
    @Test
    void deleteAll() {
        pieceDao.deleteAll();

        int actual = pieceDao.selectAll().size();
        int expected = 0;

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("기물 정보를 변경한다.")
    @Test
    void update() {
        pieceDao.update(new PieceDto(new Queen(Color.WHITE), new Position("a1")));

        List<PieceDto> pieceDtos = pieceDao.selectAll();
        String actual = pieceDtos.get(0).getPieceType();
        String expected = "Q";

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void selectAll() {
        List<PieceDto> pieceDtos = pieceDao.selectAll();
        int actual = pieceDtos.size();
        int expected = 1;

        assertThat(actual).isEqualTo(expected);
    }
}
