package chess.dao;

import chess.domain.Team;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.position.Column;
import chess.domain.position.Position;
import chess.domain.position.Row;
import chess.dto.request.GameIdRequest;
import chess.dto.response.PieceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;


@JdbcTest
class ChessBoardDaoTest {

    private ChessBoardDao chessBoardDao;
    private ChessRoomDao chessRoomDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessBoardDao = new ChessBoardDao(jdbcTemplate);
        chessRoomDao = new ChessRoomDao(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE board IF EXISTS");
        jdbcTemplate.execute("DROP TABLE room IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS room(" +
                "  id bigint NOT NULL AUTO_INCREMENT,\n" +
                "  status varchar(50) NOT NULL,\n" +
                "  name varchar(50) NOT NULL,\n" +
                "  password varchar(20) NOT NULL,\n" +
                "  PRIMARY KEY (id)" +
                ")");

        jdbcTemplate.update("insert into room (id, status, name, password) values(?, ?, ?, ?)",
                1000, "WHITE", "green", "1234");

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
        return chessRoomDao.findById(new GameIdRequest(1000L)).getId();
    }

    @Test
    void findAll() {
        List<PieceResponse> pieces = chessBoardDao.findAllPiece(roomId());
        assertThat(pieces).hasSize(3);
    }

    @Test
    void saveAll() {
        Map<Position, Piece> board = Map.of(Position.of(Column.A, Row.EIGHT), new Pawn(Team.BLACK));
        assertThatNoException().isThrownBy(() ->
                chessBoardDao.savePieces(board, roomId()));
    }

    @Test
    void updatePosition() {
        assertThatNoException().isThrownBy(() ->
                chessBoardDao.updatePosition("a7", "a6", roomId()));
    }

   @Test
   void deleteAllPiece() {
        assertThatNoException().isThrownBy(() ->
                chessBoardDao.deleteBoard());

   }
}

