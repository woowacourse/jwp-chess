package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.dao.ChessPieceDao;
import chess.dao.RoomDao;
import chess.domain.GameStatus;
import chess.domain.chesspiece.ChessPiece;
import chess.domain.chesspiece.Color;
import chess.domain.chesspiece.King;
import chess.domain.chesspiece.Knight;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class ChessServiceTest {

    private RoomDao roomDao;
    private ChessPieceDao chessPieceDao;

    @Autowired
    private ChessService chessService;

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
    @DisplayName("방에 해당하는 기물이 존재하지 않으면 예외가 터진다.")
    void findAllPiece_exception() {
        // given
        final int roomId = 1;

        // when

        // then
        assertThatThrownBy(() -> chessService.findAllPiece(roomId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("기물이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("방에 해당하는 모든 기물을 조회한다.")
    void findAllPiece() {
        // given
        final int roomId = roomDao.save("test", GameStatus.READY, Color.WHITE, "1234");

        final Map<Position, ChessPiece> pieceByPosition = new HashMap<>();
        pieceByPosition.put(Position.from("a1"), King.from(Color.WHITE));
        pieceByPosition.put(Position.from("a2"), Queen.from(Color.WHITE));
        pieceByPosition.put(Position.from("a3"), Knight.from(Color.WHITE));

        chessPieceDao.saveAll(roomId, pieceByPosition);

        // when
        final List<ChessPieceDto> actual = chessService.findAllPiece(roomId);

        // then
        assertThat(actual.size()).isEqualTo(3);
    }
}