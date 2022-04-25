package chess.dao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;
import chess.dto.ChessPieceDto;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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
        chessPieceDao = new ChessPieceDao(jdbcTemplate);
        roomDao = new RoomDao(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE IF EXISTS room");
        jdbcTemplate.execute(""
                + "CREATE TABLE room"
                + "("
                + "    room_id      INT         PRIMARY KEY AUTO_INCREMENT,"
                + "    name         VARCHAR(10) NOT NULL UNIQUE,"
                + "    game_status  VARCHAR(10) NOT NULL,"
                + "    current_turn VARCHAR(10) NOT NULL,"
                + "    password     VARCHAR(10) NOT NULL"
                + ")");

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
}