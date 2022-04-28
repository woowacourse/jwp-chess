package chess.web.dao;

import static org.assertj.core.api.Assertions.*;

import chess.domain.game.ChessGame;
import chess.domain.piece.Piece;
import chess.domain.piece.StartedPawn;
import chess.domain.piece.position.Position;
import chess.domain.piece.property.Color;
import chess.web.dto.ChessCellDto;
import chess.web.dto.MoveDto;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class ChessBoardDaoImplTest {

    private ChessBoardDao chessBoardDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessBoardDao = new ChessBoardDaoImpl(jdbcTemplate);

        jdbcTemplate.execute("create table room "
                + "("
                + "    id int(10) NOT NULL AUTO_INCREMENT, "
                + "    title varchar(255), "
                + "    password varchar(255) NOT NULL, "
                + "    color varchar(5) NOT NULL DEFAULT 'WHITE', "
                + "    finished boolean default 0, "
                + "    deleted boolean default 0,"
                + "    primary key (id) "
                + ");");

        jdbcTemplate.execute("insert into room (title, password) values ('testTitle', 'testPassword')");

        jdbcTemplate.execute("create table board "
                + "("
                + "    board_id int(10) NOT NULL AUTO_INCREMENT, "
                + "    position varchar(2) NOT NULL, "
                + "    piece varchar(10) NOT NULL, "
                + "    room_id int(10) NOT NULL, "
                + "    primary key (board_id), "
                + "    foreign key (room_id) references room (id)"
                + ");");

        ChessGame chessGame = new ChessGame();
        chessGame.start();
        Map<Position, Piece> chessBoard = chessGame.getBoard();
        for (Position position : chessBoard.keySet()) {
            chessBoardDao.save(position, chessBoard.get(position), 1);
        }
    }

    @AfterEach
    void cleanUp() {
        jdbcTemplate.execute("DROP TABLE board IF EXISTS");
        jdbcTemplate.execute("DROP TABLE room IF EXISTS");
    }

    @Test
    void save() {
        Position position = Position.of("a2");
        Piece piece = new StartedPawn(Color.WHITE);

        chessBoardDao.save(position, piece, 1);

        Map<Position, Piece> board = chessBoardDao.findAllPieces(1);
        assertThat(board).hasSize(32);
    }

    @Test
    void findAllPieces() {
        Map<Position, Piece> board = chessBoardDao.findAllPieces(1);
        Set<Position> positions = board.keySet();
        for (Position position : positions) {
            System.out.println(position.toString());
            System.out.println(board.get(position));
        }
        assertThat(board).isNotNull();
    }

    @Test
    void movePiece() {
        MoveDto moveDto = new MoveDto("A2", "A4");

        chessBoardDao.movePiece(moveDto, 1);

        assertThat(chessBoardDao.findByPosition(1, "A4")).isNotNull();
    }

    @Test
    void findByPosition() {
        ChessCellDto chessCellDto = chessBoardDao.findByPosition(1, "A2");

        assertThat(chessCellDto.getPiece()).isEqualTo("p_WHITE");
    }

    @Test
    void boardExistInRoom() {
        boolean result = chessBoardDao.boardExistInRoom(1);

        assertThat(result).isTrue();
    }
}
