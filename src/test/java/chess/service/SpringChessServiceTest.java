package chess.service;

import chess.domain.board.Board;
import chess.domain.board.Point;
import chess.domain.board.Team;
import chess.domain.chessgame.ChessGame;
import chess.domain.piece.Piece;
import chess.dto.web.*;
import chess.repository.RoomRepository;
import chess.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestPropertySource("classpath:application.properties")
class SpringChessServiceTest {
    SpringChessService springChessService;
    RoomRepository roomRepository;
    UserRepository userRepository;
    JdbcTemplate jdbcTemplate;

    public SpringChessServiceTest(
            final SpringChessService springChessService,
            final RoomRepository roomRepository,
            final UserRepository userRepository,
            final JdbcTemplate jdbcTemplate
    ) {
        this.springChessService = springChessService;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DROP TABLE users");
        jdbcTemplate.update("DROP TABLE room");
        jdbcTemplate.update("DROP TABLE play_log");

        jdbcTemplate.update(
                "CREATE TABLE IF NOT EXISTS users (\n" +
                        "    name varchar(255) NOT NULL PRIMARY KEY,\n" +
                        "    win int(11) NOT NULL default 0,\n" +
                        "    lose int(11) NOT NULL default 0\n" +
                        ")"
        );

        jdbcTemplate.update(
                "CREATE TABLE IF NOT EXISTS room (\n" +
                        "    id int NOT NULL PRIMARY KEY AUTO_INCREMENT,\n" +
                        "    name varchar(255) NOT NULL,\n" +
                        "    is_opened boolean NOT NULL,\n" +
                        "    white varchar(255) NOT NULL,\n" +
                        "    black varchar(255) NOT NULL\n" +
                        ")"
        );

        jdbcTemplate.update(
                "CREATE TABLE IF NOT EXISTS play_log (\n" +
                        "    id int NOT NULL PRIMARY KEY AUTO_INCREMENT,\n" +
                        "    board clob NOT NULL,\n" +
                        "    game_status clob NOT NULL,\n" +
                        "    room_id int NOT NULL,\n" +
                        "    last_played_time timestamp default NOW()\n" +
                        ")"
        );

        roomRepository.insert(new RoomDto("1", "fortuneRoom", "fortune", "portune"));
        roomRepository.insert(new RoomDto("2", "rootRoom", "root", "loot"));
        userRepository.insert("fortune");
        userRepository.insert("portune");
        userRepository.insert("root");
        userRepository.insert("loot");
    }

    @DisplayName("열려있는 방을 호출하면, is_opened가 true인 방의 리스트를 반환한다.")
    @Test
    void openedRooms() {
        RoomDto expectRoomDto1 = new RoomDto("1", "fortuneRoom", "fortune", "portune");
        RoomDto expectRoomDto2 = new RoomDto("2", "rootRoom", "root", "loot");

        assertThat(springChessService.openedRooms()).isNotEmpty();
        assertThat(springChessService.openedRooms()).usingRecursiveComparison().isEqualTo(
                Arrays.asList(expectRoomDto1, expectRoomDto2)
        );
    }

    @DisplayName("최근에 사용한 방을 호출하면, id에 맞는 최근 방의 정보를 반환한다")
    @ParameterizedTest
    @CsvSource({"2"})
    void latestBoard(final String id) {
        BoardDto expectedBoardDto = new BoardDto(new Board());
        assertThat(springChessService.latestBoard(id)).usingRecursiveComparison().isEqualTo(
                expectedBoardDto
        );
    }

    @DisplayName("room에 존재하는 유저들의 전적을 요청하면, 각 userName별 전적을 반환한다.")
    @Test
    void usersInRoom() {
        UsersInRoomDto expectedUsersInRoomDto = new UsersInRoomDto(
                "fortune", "0", "0",
                "portune", "0", "0"
        );
        UsersInRoomDto usersInFirstRoomDto = springChessService.usersInRoom("1");
        assertThat(usersInFirstRoomDto).usingRecursiveComparison().isEqualTo(expectedUsersInRoomDto);
    }

    @DisplayName("room을 생성하면, id가 자동 생성된 roomDto를 반환한다.")
    @Test
    void create() {
        RoomIdDto expectedRoomIdDto = new RoomIdDto("3");
        RoomDto roomDto = new RoomDto("", "daveRoom", "dave", "dabe");
        assertThat(springChessService.create(roomDto)).usingRecursiveComparison().isEqualTo(expectedRoomIdDto);
    }

    @DisplayName("게임 상태를 요청하면, roomId에 맞는 GameStatusDto를 반환한다.")
    @Test
    void gameStatus() {
        GameStatusDto expectedGameStatusDto = new GameStatusDto(new ChessGame(new Board()));
        assertThat(springChessService.gameStatus("1")).usingRecursiveComparison().isEqualTo(expectedGameStatusDto);
    }

    @DisplayName("게임을 시작하면, 해당 게임의 상태는 Running이 된다.")
    @Test
    void gameStart() {
        springChessService.start("1");
        GameStatusDto expectedGameStatusDto = springChessService.gameStatus("1");
        assertThat(expectedGameStatusDto.getGameState()).isEqualTo("Running");
    }

    @DisplayName("게임을 끝내면, 해당 게임 상태는 Finished가 된다.")
    @Test
    void exit() {
        springChessService.start("1");
        springChessService.exit("1");
        GameStatusDto expectedGameStatusDto = springChessService.gameStatus("1");
        assertThat(expectedGameStatusDto.getGameState()).isEqualTo("Finished");
    }

    @DisplayName("말의 위치를 보내면, 해당 말이 갈 수 있는 경로를 반환한다. ")
    @Test
    void movablePoints() {
        springChessService.start("1");
        List<PointDto> expectedPointDtoList = springChessService.movablePoints("1", "b2");
        assertThat(expectedPointDtoList).usingRecursiveFieldByFieldElementComparator().containsAll(
                Arrays.asList(
                        new PointDto(Point.of("b3")),
                        new PointDto(Point.of("b4"))
                )
        );
    }

    @DisplayName("말을 움직이는데 성공하면, 변환된 보드 정보를 반환한다.")
    @Test
    void move() {
        springChessService.start("1");
        BoardDto boardDto = springChessService.move("1", "b2", "b3");
        assertThat(boardDto.getBoard()).usingRecursiveFieldByFieldElementComparator().contains(new PieceDto(
                Point.of("b3"),
                Team.WHITE,
                Piece.PAWN
        ));
    }
}