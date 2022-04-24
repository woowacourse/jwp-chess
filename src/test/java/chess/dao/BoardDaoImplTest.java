package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.board.Board;
import chess.domain.game.GameId;
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
    private static final GameId GAME_ID = GameId.from("test-game");
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

        GameDaoImpl gameDao = new GameDaoImpl(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE game, board IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE game("
                + "id   VARCHAR(36) NOT NULL,"
                + "turn ENUM('WHITE', 'BLACK'),"
                + "PRIMARY KEY (id))"
        );

        jdbcTemplate.execute("CREATE TABLE board("
                + "game_id     VARCHAR(36) NOT NULL,"
                + "x_axis      ENUM('1', '2', '3', '4', '5', '6', '7', '8'),"
                + "y_axis      ENUM('1', '2', '3', '4', '5', '6', '7', '8'),"
                + "piece_type  ENUM('PAWN', 'ROOK', 'KNIGHT', 'BISHOP', 'QUEEN', 'KING'),"
                + "piece_color ENUM('WHITE', 'BLACK'),"
                + "PRIMARY KEY (game_id, x_axis, y_axis),"
                + "FOREIGN KEY (game_id) REFERENCES game (id) ON DELETE CASCADE)"
        );
        gameDao.createGame(GAME_ID);
    }

    @DisplayName("getBoard 는 Board 를 반환한다.")
    @Test
    void getBoard() {
        // given & when
        Board board = boardDao.getBoard(GAME_ID);

        // then
        assertThat(board).isInstanceOf(Board.class);
    }

    @DisplayName("board 테이블에 기물을 생성한다.")
    @Test
    void createPiece() {
        boardDao.createPiece(GAME_ID, Position.of(X_AXIS, Y_AXIS),
                new Piece(PIECE_TYPE, PIECE_COLOR));
    }

    @DisplayName("board 테이블의 기물을 제거한다.")
    @Test
    void deletePiece() {
        // given
        boardDao.createPiece(GAME_ID, Position.of(X_AXIS, Y_AXIS), new Piece(PIECE_TYPE, PIECE_COLOR));

        // when & then
        boardDao.deletePiece(GAME_ID, Position.of(X_AXIS, Y_AXIS));
    }

    @DisplayName("board 테이블의 특정 기물 위치를 변경한다.")
    @Test
    void updatePiecePosition() {
        // given
        boardDao.createPiece(GAME_ID, Position.of(X_AXIS, Y_AXIS), new Piece(PIECE_TYPE, PIECE_COLOR));

        // then
        boardDao.updatePiecePosition(GAME_ID, Position.of(X_AXIS, Y_AXIS), Position.of(X_AXIS_2, Y_AXIS_2));
    }
}
