package chess.web.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.game.ChessGame;
import chess.domain.piece.Piece;
import chess.domain.piece.position.Position;
import chess.web.dao.ChessBoardDao;
import chess.web.dao.ChessBoardDaoImpl;
import chess.web.dao.RoomDao;
import chess.web.dao.RoomDaoImpl;
import chess.web.dto.ChessCellDto;
import chess.web.dto.CreateRoomRequestDto;
import chess.web.dto.CreateRoomResultDto;
import chess.web.dto.DeleteDto;
import chess.web.dto.DeleteResultDto;
import chess.web.dto.MoveDto;
import chess.web.dto.MoveResultDto;
import chess.web.dto.PlayResultDto;
import chess.web.dto.ReadRoomResultDto;
import chess.web.dto.RoomDto;
import chess.web.dto.StartResultDto;
import chess.web.service.fakedao.FakeChessBoardDao;
import chess.web.service.fakedao.FakePlayerDao;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class ChessGameServiceTest {

    private ChessBoardDao chessBoardDao;
    private RoomDao roomDao;
    private ChessGameService chessGameService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessBoardDao = new ChessBoardDaoImpl(jdbcTemplate);
        roomDao = new RoomDaoImpl(jdbcTemplate);
        chessGameService = new ChessGameService(chessBoardDao, roomDao);

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
    void start() {
        RoomDto roomDto = chessGameService.start(1);
        assertThat(roomDto).isNotNull();
    }

    @Test
    void move() {
        MoveResultDto moveResultDto = chessGameService.move(new MoveDto("A2", "A4"), 1);
        ChessCellDto chessCellDto = chessBoardDao.findByPosition(1, "A4");
        assertThat(chessCellDto).isNotNull();
    }

    @Test
    void play() {
        PlayResultDto playResultDto = chessGameService.play(1);

        assertThat(playResultDto.getBoard().size()).isEqualTo(32);
    }

    @Test
    void getChessGame() {
        ChessGame chessGame = chessGameService.getChessGame(1);
        assertThat(chessGame.getBoard().size()).isEqualTo(32);
    }

    @Test
    void createRoom() {
        CreateRoomRequestDto createRoomRequestDto = new CreateRoomRequestDto("testTitle", "testPassword");
        CreateRoomResultDto room = chessGameService.createRoom(createRoomRequestDto);
        assertThat(room).isNotNull();
    }

    @Test
    void findAllRooms() {
        ReadRoomResultDto readRoomResultDto = chessGameService.findAllRooms();
        assertThat(readRoomResultDto.getRooms().size()).isEqualTo(1);
    }

    @Test
    void delete() {
        DeleteResultDto deleteResultDto = chessGameService.delete(1, new DeleteDto("testPassword"));
        assertThat(deleteResultDto.isDeleted()).isEqualTo(true);
    }
}
