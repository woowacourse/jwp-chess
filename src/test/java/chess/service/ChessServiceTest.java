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
import chess.dto.request.MoveRequestDto;
import chess.dto.response.ChessPieceDto;
import chess.dto.response.CurrentTurnDto;
import chess.dto.response.RoomStatusDto;
import chess.exception.NotFoundException;
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

    @Autowired
    private ChessService chessService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private ChessPieceDao chessPieceDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS room");
        jdbcTemplate.execute(""
                + "CREATE TABLE room"
                + "("
                + "    room_id      INT         PRIMARY KEY AUTO_INCREMENT,"
                + "    name         VARCHAR(10) NOT NULL,"
                + "    game_status  VARCHAR(10) NOT NULL,"
                + "    current_turn VARCHAR(10) NOT NULL,"
                + "    password     VARCHAR(255) NOT NULL,"
                + "    is_delete    BOOLEAN      NOT NULL DEFAULT FALSE"
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

    @AfterEach
    void clear() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS chess_piece");
        jdbcTemplate.execute("DROP TABLE IF EXISTS room");
    }

    @Test
    @DisplayName("방에 해당하는 기물이 존재하지 않으면 예외가 터진다.")
    void findAllPiece_exception() {
        // given
        final int roomId = roomDao.save("test", GameStatus.READY, Color.WHITE, "1234");

        // then
        assertThatThrownBy(() -> chessService.findAllPiece(roomId))
                .isInstanceOf(NotFoundException.class)
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

    @Test
    @DisplayName("기물을 이동하면 이동 시킨 기물의 위치가 변경된다.")
    void move_updatePosition() {
        // given
        final int roomId = roomDao.save("test", GameStatus.PLAYING, Color.WHITE, "1234");

        final String from = "a1";
        final String to = "b2";

        final Map<Position, ChessPiece> pieceByPosition = new HashMap<>();
        pieceByPosition.put(Position.from(from), King.from(Color.WHITE));
        chessPieceDao.saveAll(roomId, pieceByPosition);

        // when
        final MoveRequestDto dto = new MoveRequestDto(from, to);
        chessService.move(roomId, dto);

        // then
        final List<ChessPieceDto> allPiece = chessPieceDao.findAllByRoomId(roomId);
        final ChessPieceDto actual = allPiece.get(0);
        assertThat(actual.getPosition()).isEqualTo(to);
    }

    @Test
    @DisplayName("기물을 이동하면 방의 상태가 변경된다.")
    void move_updateRoom() {
        // given
        final Color initialTurn = Color.WHITE;
        final int roomId = roomDao.save("test", GameStatus.PLAYING, initialTurn, "1234");

        final String from = "a1";
        final String to = "b2";

        final Map<Position, ChessPiece> pieceByPosition = new HashMap<>();
        pieceByPosition.put(Position.from(from), King.from(Color.WHITE));
        chessPieceDao.saveAll(roomId, pieceByPosition);

        // when
        final MoveRequestDto dto = new MoveRequestDto(from, to);
        chessService.move(roomId, dto);

        // then
        final CurrentTurnDto currentTurnDto = roomDao.findCurrentTurnById(roomId);
        assertThat(currentTurnDto.getCurrentTurn()).isEqualTo(initialTurn.toOpposite());
    }

    @Test
    @DisplayName("결과를 조회하면 방 상태가 END로 변경된다.")
    void result() {
        // given
        final int roomId = roomDao.save("test", GameStatus.PLAYING, Color.WHITE, "1234");

        final Map<Position, ChessPiece> pieceByPosition = new HashMap<>();
        pieceByPosition.put(Position.from("a1"), King.from(Color.WHITE));
        chessPieceDao.saveAll(roomId, pieceByPosition);

        // when
        chessService.result(roomId);

        // then
        final RoomStatusDto statusDto = roomDao.findStatusById(roomId);
        assertThat(statusDto.getGameStatus()).isEqualTo(GameStatus.END);
    }
}
