package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.board.Board;
import chess.domain.game.room.Room;
import chess.domain.game.room.RoomId;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceColor;
import chess.domain.piece.PieceType;
import chess.domain.position.Position;
import chess.domain.position.XAxis;
import chess.domain.position.YAxis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class BoardDaoImplTest {
    private static final String TEST_GAME_ROOM_ID = "TEST-GAME-ID";
    private static final String TEST_GAME_ROOM_TITLE = "TEST-GAME-ROOM";
    private static final String TEST_GAME_ROOM_PASSWORD = "1234";
    private static final XAxis X_AXIS = XAxis.A;
    private static final YAxis Y_AXIS = YAxis.ONE;
    private static final XAxis X_AXIS_2 = XAxis.B;
    private static final YAxis Y_AXIS_2 = YAxis.TWO;
    private static final PieceType PIECE_TYPE = PieceType.PAWN;
    private static final PieceColor PIECE_COLOR = PieceColor.WHITE;

    private BoardDaoImpl boardDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        boardDao = new BoardDaoImpl(jdbcTemplate);

        GameRoomDaoImpl gameDao = new GameRoomDaoImpl(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE game_room, board IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE game_room("
                + "id       VARCHAR(36) NOT NULL,"
                + "title    VARCHAR(50) NOT NULL,"
                + "password VARCHAR(64) NOT NULL,"
                + "turn ENUM('WHITE', 'BLACK'),"
                + "PRIMARY KEY (id))"
        );

        jdbcTemplate.execute("CREATE TABLE board("
                + "game_room_id     VARCHAR(36) NOT NULL,"
                + "x_axis      ENUM('1', '2', '3', '4', '5', '6', '7', '8'),"
                + "y_axis      ENUM('1', '2', '3', '4', '5', '6', '7', '8'),"
                + "piece_type  ENUM('PAWN', 'ROOK', 'KNIGHT', 'BISHOP', 'QUEEN', 'KING'),"
                + "piece_color ENUM('WHITE', 'BLACK'),"
                + "PRIMARY KEY (game_room_id, x_axis, y_axis),"
                + "FOREIGN KEY (game_room_id) REFERENCES game_room (id) ON DELETE CASCADE)"
        );

        gameDao.createGameRoom(Room.from(TEST_GAME_ROOM_ID, TEST_GAME_ROOM_TITLE, TEST_GAME_ROOM_PASSWORD));
    }

    @DisplayName("getBoard 는 Board 를 반환한다.")
    @Test
    void getBoard() {
        // given & when
        Board board = boardDao.getBoard(RoomId.from(TEST_GAME_ROOM_ID));

        // then
        assertThat(board).isInstanceOf(Board.class);
    }

    @DisplayName("board 테이블에 기물을 생성한다.")
    @Test
    void createPiece() {
        boardDao.createPiece(RoomId.from(TEST_GAME_ROOM_ID), Position.of(X_AXIS, Y_AXIS),
                new Piece(PIECE_TYPE, PIECE_COLOR));
    }

    @DisplayName("board 테이블의 기물을 제거한다.")
    @Test
    void deletePiece() {
        // given
        boardDao.createPiece(RoomId.from(TEST_GAME_ROOM_ID), Position.of(X_AXIS, Y_AXIS),
                new Piece(PIECE_TYPE, PIECE_COLOR));

        // when & then
        boardDao.deletePiece(RoomId.from(TEST_GAME_ROOM_ID), Position.of(X_AXIS, Y_AXIS));
    }

    @DisplayName("board 테이블의 특정 기물 위치를 변경한다.")
    @Test
    void updatePiecePosition() {
        // given
        boardDao.createPiece(RoomId.from(TEST_GAME_ROOM_ID), Position.of(X_AXIS, Y_AXIS),
                new Piece(PIECE_TYPE, PIECE_COLOR));

        // then
        boardDao.updatePiecePosition(RoomId.from(TEST_GAME_ROOM_ID), Position.of(X_AXIS, Y_AXIS),
                Position.of(X_AXIS_2, Y_AXIS_2));
    }
}
