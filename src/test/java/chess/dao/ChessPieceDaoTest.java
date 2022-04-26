package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.GameStatus;
import chess.domain.chesspiece.ChessPiece;
import chess.domain.chesspiece.Color;
import chess.domain.chesspiece.King;
import chess.domain.chesspiece.Pawn;
import chess.domain.chesspiece.Queen;
import chess.domain.position.Position;
import chess.dto.response.ChessPieceDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class ChessPieceDaoTest {

    private ChessPieceDao chessPieceDao;
    private RoomDao roomDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        roomDao = new RoomDao(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE IF EXISTS room");
        jdbcTemplate.execute(""
                + "CREATE TABLE room"
                + "("
                + "    room_id      INT         PRIMARY KEY AUTO_INCREMENT,"
                + "    name         VARCHAR(10) NOT NULL UNIQUE,"
                + "    game_status  VARCHAR(10) NOT NULL,"
                + "    current_turn VARCHAR(10) NOT NULL,"
                + "    password     VARCHAR(255) NOT NULL"
                + ")");

        chessPieceDao = new ChessPieceDao(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE IF EXISTS chess_piece");
        jdbcTemplate.execute(""
                + "CREATE TABLE chess_piece"
                + "("
                + "    chess_piece_id INT         PRIMARY KEY AUTO_INCREMENT,"
                + "    room_id        INT         NOT NULL,"
                + "    position       VARCHAR(10) NOT NULL,"
                + "    chess_piece    VARCHAR(10) NOT NULL,"
                + "    color          VARCHAR(10) NOT NULL,"
                + "    FOREIGN KEY (room_id) REFERENCES room (room_id) ON DELETE CASCADE"
                + ")");
    }

    @AfterEach
    void clear() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS chess_piece");
        jdbcTemplate.execute("DROP TABLE IF EXISTS room");
    }

    @Test
    @DisplayName("roomId로 모든 기물을 조회한다.")
    void findAllByRoomId() {
        // given
        final int roomId = roomDao.save("hi", GameStatus.READY, Color.WHITE, "1q2w3e4r");

        // when
        final List<ChessPieceDto> result = chessPieceDao.findAllByRoomId(roomId);

        // then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("모든 기물을 저장한다.")
    void saveAll() {
        // given
        final int roomId = roomDao.save("hi", GameStatus.READY, Color.WHITE, "1q2w3e4r");
        final Map<Position, ChessPiece> pieceByPosition = new HashMap<>();
        pieceByPosition.put(Position.from("a1"), King.from(Color.WHITE));
        pieceByPosition.put(Position.from("a2"), King.from(Color.WHITE));

        // when
        final int insertedRow = chessPieceDao.saveAll(roomId, pieceByPosition);

        // then
        assertThat(insertedRow).isEqualTo(2);
    }

    @Test
    @DisplayName("방 id와 위치에 해당하는 기물을 삭제한다.")
    void deleteByRoomIdAndPosition() {
        // given
        final int roomId = roomDao.save("hi", GameStatus.READY, Color.WHITE, "1q2w3e4r");

        final Map<Position, ChessPiece> pieceByPosition = new HashMap<>();
        final Position position = Position.from("a1");
        pieceByPosition.put(position, King.from(Color.WHITE));
        chessPieceDao.saveAll(roomId, pieceByPosition);

        // when
        final int deletedRow = chessPieceDao.deleteByRoomIdAndPosition(roomId, position);

        // then
        assertThat(deletedRow).isEqualTo(1);
    }

    @Test
    @DisplayName("방 id에 해당하는 모든 기물을 삭제한다.")
    void deleteAllByRoomId() {
        // given
        final int roomId = roomDao.save("hi", GameStatus.READY, Color.WHITE, "1q2w3e4r");

        final Map<Position, ChessPiece> pieceByPosition = new HashMap<>();
        pieceByPosition.put(Position.from("a1"), King.from(Color.WHITE));
        pieceByPosition.put(Position.from("a2"), Queen.from(Color.WHITE));
        pieceByPosition.put(Position.from("a3"), Pawn.from(Color.WHITE));
        chessPieceDao.saveAll(roomId, pieceByPosition);

        // when
        final int deletedRow = chessPieceDao.deleteAllByRoomId(roomId);

        // then
        assertThat(deletedRow).isEqualTo(3);
    }

    @Test
    @DisplayName("방 id와 위치에 해당하는 기물의 위치를 갱신한다.")
    void updateByRoomIdAndPosition() {
        // given
        final int roomId = roomDao.save("hi", GameStatus.READY, Color.WHITE, "1q2w3e4r");

        final Map<Position, ChessPiece> pieceByPosition = new HashMap<>();
        final Position from = Position.from("a1");
        pieceByPosition.put(from, King.from(Color.WHITE));
        chessPieceDao.saveAll(roomId, pieceByPosition);

        // when
        final Position to = Position.from("b2");
        final int updatedRow = chessPieceDao.updateByRoomIdAndPosition(roomId, from, to);

        // then
        assertThat(updatedRow).isEqualTo(1);
    }
}