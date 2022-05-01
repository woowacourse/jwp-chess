package chess.dao;

import chess.domain.state.BoardInitialize;
import chess.dto.response.PieceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@JdbcTest
class BoardDaoImplTest {

    private BoardDaoImpl boardDaoImpl;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        boardDaoImpl = new BoardDaoImpl(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE board IF EXISTS");
        jdbcTemplate.execute("DROP TABLE room IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS room" +
            "(" +
            "  id bigint AUTO_INCREMENT," +
            "  team varchar(50) NOT NULL," +
            "  title varchar(50) NOT NULL," +
            "  password varchar(50) NOT NULL," +
            "  status boolean," +
            " PRIMARY KEY (id)" +
            ")");

        jdbcTemplate.execute("CREATE TABLE board(\n" +
            "  id bigint NOT NULL AUTO_INCREMENT,\n" +
            "  position varchar(50) NOT NULL,\n" +
            "  symbol varchar(50) NOT NULL,\n" +
            "  room_id bigint NOT NULL,\n" +
            "  PRIMARY KEY (id),\n" +
            "  CONSTRAINT id FOREIGN KEY (room_id) REFERENCES room (id) ON DELETE CASCADE ON UPDATE CASCADE)");
        List<Object[]> pieces = Arrays.asList("a7 P 1", "b7 P 1", "a2 p 1").stream()
            .map(piece -> piece.split(" "))
            .collect(Collectors.toList());

        jdbcTemplate.update(
            "insert into room (id, team, title, password, status) values(?, ?, ?, ?, ?)", 1,
            "WHITE", "제목", "비밀번호", true);
        jdbcTemplate.update(
            "insert into room (id, team, title, password, status) values(?, ?, ?, ?, ?)", 2,
            "WHITE", "제목", "비밀번호", true);
        jdbcTemplate.batchUpdate("insert into board (position, symbol, room_id) values(?, ?, ?)",
            pieces);
    }

    @Test
    void findAll() {
        List<PieceDto> pieces = boardDaoImpl.findAll(1L);
        assertThat(pieces.size()).isEqualTo(3);
    }

    @Test
    void saveAll() {
        boardDaoImpl.saveAll(BoardInitialize.create(), 2L);
        List<PieceDto> pieces = boardDaoImpl.findAll(2L);
        assertThat(pieces.size()).isEqualTo(64);
    }

    @Test
    void delete() {
        assertThatNoException().isThrownBy(() -> boardDaoImpl.delete(1L));
    }

    @Test
    void updatePosition() {
        assertThatNoException().isThrownBy(() ->  boardDaoImpl.updatePosition(".", "a2", 2L));
    }
}
