package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.dao.GameDao;
import chess.dao.JdbcGameDao;
import chess.dao.JdbcRoomDao;
import chess.dao.PieceDao;
import chess.dao.JdbcPieceDao;
import chess.dao.RoomDao;
import chess.dao.TurnDao;
import chess.dao.JdbcTurnDao;
import chess.dto.MoveDto;
import chess.dto.RoomDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class ChessServiceTest {

    private ChessService chessService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        PieceDao pieceDao = new JdbcPieceDao(jdbcTemplate);
        TurnDao turnDao = new JdbcTurnDao(jdbcTemplate);
        RoomDao roomDao = new JdbcRoomDao(jdbcTemplate);
        GameDao gameDao = new JdbcGameDao(jdbcTemplate);

        this.chessService = new ChessService(pieceDao, turnDao, roomDao, gameDao);

        jdbcTemplate.execute("drop table turn if exists");
        jdbcTemplate.execute("drop table piece if exists");
        jdbcTemplate.execute("drop table game if exists");
        jdbcTemplate.execute("drop table room if exists");
        jdbcTemplate.execute("CREATE TABLE room\n"
                + "(\n"
                + "    id       int         not null auto_increment primary key,\n"
                + "    title    varchar(30) not null,\n"
                + "    password varchar(8)  not null\n"
                + ");\n"
                + "CREATE TABLE game\n"
                + "(\n"
                + "    roomId int        not null primary key,\n"
                + "    state   varchar(3) not null,\n"
                + "    foreign key (roomId) REFERENCES room (id)\n"
                + "        on delete cascade\n"
                + ");\n"
                + "CREATE TABLE turn\n"
                + "(\n"
                + "    roomId int        not null primary key,\n"
                + "    team   varchar(5) not null,\n"
                + "    foreign key (roomId) REFERENCES room (id)\n"
                + "        on delete cascade\n"
                + ");\n"
                + "CREATE TABLE piece\n"
                + "(\n"
                + "    roomId   int        not null,\n"
                + "    position varchar(3) not null,\n"
                + "    name     varchar(2) not null,\n"
                + "    team     varchar(5) not null,\n"
                + "    primary key (roomId, position),\n"
                + "    foreign key (roomId) REFERENCES room (id)\n"
                + "        on delete cascade\n"
                + ");\n");
        RoomDto roomDto = chessService.createRoom(new RoomDto("title", "password"));
        chessService.initializeGame(roomDto.getId());
    }

    @Test
    void initializeGame() {
        List<RoomDto> roomList = chessService.getRooms();
        assertThat(chessService.initializeGame(roomList.get(0).getId())).isNotNull();
    }

    @Test
    void load() {
        List<RoomDto> roomList = chessService.getRooms();
        RoomDto roomDto = roomList.get(0);
        assertThat(chessService.load(roomDto.getId())).isNotNull();
    }

    @Test
    void move() {
        List<RoomDto> roomList = chessService.getRooms();
        RoomDto roomDto = roomList.get(0);
        assertThat(chessService.move(roomDto.getId(), new MoveDto("a2", "a3"))).isNotNull();
    }

    @Test
    void getStatus() {
        List<RoomDto> roomList = chessService.getRooms();
        RoomDto roomDto = roomList.get(0);
        assertThat(chessService.getStatus(roomDto.getId()).getScoreStatus()).isEqualTo("WHITE: 38.0\n" + "BLACK: 38.0");
    }

    @Test
    void getResult() {
        List<RoomDto> roomList = chessService.getRooms();
        RoomDto roomDto = roomList.get(0);
        assertThat(chessService.getResult(roomDto.getId()).getResult()).isEqualTo("무승부입니다!");
    }

    @Test
    @DisplayName("end 상태일 때 삭제한다.")
    void deleteEndState() {
        chessService.endGame(1);
        chessService.deleteRoom(new RoomDto(1, "title", "password"));
        assertThat(chessService.getRooms()).hasSize(0);
    }

    @Test
    @DisplayName("end 상태가 아닐 때 삭제를 못한다.")
    void deleteRunState() {
        assertThatThrownBy(() -> chessService.deleteRoom(new RoomDto(1, "title", "password"))).isInstanceOf(
                IllegalArgumentException.class);
    }
}