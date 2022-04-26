package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.dao.ChessPieceDao;
import chess.dao.RoomDao;
import chess.domain.ChessGame;
import chess.domain.GameStatus;
import chess.domain.chessboard.ChessBoard;
import chess.domain.chessboard.ChessBoardFactory;
import chess.domain.chesspiece.ChessPiece;
import chess.domain.chesspiece.Color;
import chess.domain.chesspiece.King;
import chess.domain.chesspiece.Knight;
import chess.domain.chesspiece.Queen;
import chess.domain.position.Position;
import chess.domain.result.StartResult;
import chess.dto.response.ChessPieceDto;
import chess.dto.response.RoomStatusDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    /*
    * 방이 존재하지 않으면 예외가 터진다.
    * 방이 READY 가 아니라면 예외가 터진다.
    * 초기 기물을 세팅하고 DB에 저장한다. (32개)
    * 방 상태를 업데이트한다. (PLAYING, WHITE)
    * */

    @Test
    @DisplayName("방이 존재하지 않으면 예외가 터진다.")
    void initPiece_room_not_exist() {
        // given
        final int roomId = 1;

        // then
        assertThatThrownBy(() -> chessService.initPiece(roomId))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("방이 존재하지 않습니다.");
    }

    @ParameterizedTest
    @DisplayName("방의 상태가 READY 가 아니라면 예외가 터진다.")
    @CsvSource(value = {"PLAYING", "END", "KING_DIE"})
    void initPiece_not_READY(final GameStatus gameStatus) {
        // given
        final int roomId = roomDao.save("test", gameStatus, Color.WHITE, "1234");

        // then
        assertThatThrownBy(() -> chessService.initPiece(roomId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게임이 이미 시작한 후에는 기물을 초기화 할 수 없습니다.");
    }

    @Test
    @DisplayName("기물을 초기화 하면 방의 상태가 PLAYING이 된다.")
    void initPiece_gameStatus() {
        // given
        final int roomId = roomDao.save("test", GameStatus.READY, Color.WHITE, "1234");

        // when
        chessService.initPiece(roomId);

        // then
        final RoomStatusDto actual = roomDao.findStatusById(roomId);
        assertThat(actual.getGameStatus()).isEqualTo(GameStatus.PLAYING);
    }

    @Test
    @DisplayName("기물을 초기화 하면 방에 32개의 기물이 존재한다.")
    void initPiece_pieces() {
        // given
        final int roomId = roomDao.save("test", GameStatus.READY, Color.WHITE, "1234");

        // when
        chessService.initPiece(roomId);

        // then
        final List<ChessPieceDto> actual = chessPieceDao.findAllByRoomId(roomId);
        assertThat(actual.size()).isEqualTo(32);
    }
}