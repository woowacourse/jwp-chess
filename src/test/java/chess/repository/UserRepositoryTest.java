package chess.repository;

import chess.domain.board.Team;
import chess.dto.web.RoomDto;
import chess.dto.web.UsersInRoomDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Sql("classpath:tableInit.sql")
class UserRepositoryTest {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.userRepository = new UserRepository(jdbcTemplate);
        this.roomRepository = new RoomRepository(jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("INSERT INTO users (name) values ('fortune')");
        jdbcTemplate.update("INSERT INTO users (name) values ('root')");
        jdbcTemplate.update("INSERT INTO room (id, name, is_opened, white, black) VALUES (1, 'fortuneRooms', true, 'fortune', 'root')");
    }


    @DisplayName("insert를 하면, 유저네임이 테이블에 들어간다.")
    @Test
    void insert() {
        userRepository.insert("loot");
    }

    @DisplayName("room에 존재하지 않는 이름을 찾으면, true를 반환한다.")
    @Test
    void isNotExist() {
        assertThat(userRepository.isNotExist("daveJJang")).isTrue();
    }

    @DisplayName("room에 존재하는 유저들의 전적을 요청하면, 각 userName별 전적을 반환한다.")
    @Test
    void usersInRoom() {
        UsersInRoomDto expectUsersInRoomDto = new UsersInRoomDto(
                "brown", "0", "0",
                "ryan", "0", "0"
        );

        userRepository.insert("brown");
        userRepository.insert("ryan");
        roomRepository.insert(new RoomDto("2", "brownRoom", "brown", "ryan"));

        UsersInRoomDto usersInFirstRoomDto = userRepository.usersInRoom("2");
        assertThat(usersInFirstRoomDto).usingRecursiveComparison().isEqualTo(expectUsersInRoomDto);
    }

    @DisplayName("룸에 승리자가 누군지 알려주면, 전적이 갱신된다.")
    @Test
    void updateStatistics() {
        userRepository.insert("dave");
        userRepository.insert("pobi");
        roomRepository.insert(new RoomDto("2", "daveRoom", "dave", "pobi"));

        UsersInRoomDto expectUsersInRoomDto = new UsersInRoomDto(
                "dave", "1", "0",
                "pobi", "0", "1"
        );

        userRepository.updateStatistics("2", Team.WHITE);
        UsersInRoomDto usersInFirstRoomDto = userRepository.usersInRoom("2");
        assertThat(usersInFirstRoomDto).usingRecursiveComparison().isEqualTo(expectUsersInRoomDto);
    }
}


