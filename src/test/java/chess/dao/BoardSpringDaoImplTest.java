package chess.dao;

import chess.domain.Team;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.position.Column;
import chess.domain.position.Position;
import chess.domain.position.Row;
import chess.dto.PieceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@JdbcTest
class BoardSpringDaoImplTest {

    private BoardSpringDaoImpl boardSpringDaoImpl;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        boardSpringDaoImpl = new BoardSpringDaoImpl(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE board IF EXISTS");
        jdbcTemplate.execute("DROP TABLE room IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE room" +
                "(" +
                "  id bigint NOT NULL," +
                "  status varchar(50) NOT NULL," +
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

        jdbcTemplate.update("insert into room (id, status) values(?, ?)", 1, "WHITE");
        jdbcTemplate.batchUpdate("insert into board (position, symbol, room_id) values(?, ?, ?)", pieces);
    }

    @Test
    void findAll() {
        List<PieceDto> pieces = boardSpringDaoImpl.findAll(1L);
        assertThat(pieces).hasSize(3);
    }

    @Test
    void saveAll() {
        Map<Position, Piece> board = Map.of(Position.of(Column.A, Row.EIGHT), new Pawn(Team.BLACK));
        assertThatNoException().isThrownBy(() ->
                boardSpringDaoImpl.saveAll(board, 1L));
    }

    @Test
    void updatePosition() {
        assertThatNoException().isThrownBy(() ->
                boardSpringDaoImpl.updatePosition("a7", "a6"));
    }

   // @Test
   // void deleteAllPiece() {
   //     assertThatNoException().isThrownBy(() ->
   //             boardSpringDao.delete(1L));
   // }


}