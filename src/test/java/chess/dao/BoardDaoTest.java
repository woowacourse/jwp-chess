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
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;


@JdbcTest
class BoardDaoTest {

    private BoardDao boardDao;
    private RoomDao roomDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        boardDao = new BoardDao(jdbcTemplate);
        roomDao = new RoomDao(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE board IF EXISTS");
        jdbcTemplate.execute("DROP TABLE room IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE room" +
                "(" +
                "  id bigint NOT NULL AUTO_INCREMENT," +
                "  status varchar(50) NOT NULL," +
                "  PRIMARY KEY (id)" +
                ")");

        jdbcTemplate.update("insert into room (status) values(?)", "WHITE");

        jdbcTemplate.execute("CREATE TABLE board(\n" +
                "  id bigint NOT NULL AUTO_INCREMENT,\n" +
                "  position varchar(50) NOT NULL,\n" +
                "  symbol varchar(50) NOT NULL,\n" +
                "  room_id bigint NOT NULL,\n" +
                "  PRIMARY KEY (id),\n" +
                "  CONSTRAINT id FOREIGN KEY (room_id) REFERENCES room (id) ON DELETE CASCADE ON UPDATE CASCADE)");

        List<Object[]> pieces = Stream.of("a7 P" + " " + roomId(), "b7 P"+ " " + roomId(),
                        "a2 p" + " " + roomId())
                .map(piece -> piece.split(" "))
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate("insert into board (position, symbol, room_id) values(?, ?, ?)", pieces);
    }

    private Long roomId() {
        return roomDao.findById().getId();
    }

    @Test
    void findAll() {
        List<PieceDto> pieces = boardDao.findAllPiece(roomId());
        assertThat(pieces).hasSize(3);
    }

    @Test
    void saveAll() {
        Map<Position, Piece> board = Map.of(Position.of(Column.A, Row.EIGHT), new Pawn(Team.BLACK));
        assertThatNoException().isThrownBy(() ->
                boardDao.savePieces(board, roomId()));
    }

    @Test
    void updatePosition() {
        assertThatNoException().isThrownBy(() ->
                boardDao.updatePosition("a7", "a6", roomId()));
    }

   @Test
   void deleteAllPiece() {
        assertThatNoException().isThrownBy(() ->
                boardDao.deleteBoard());

   }
}

