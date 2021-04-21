package chess.repository;

import chess.dto.RoomDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@TestPropertySource("classpath:application-test.properties")
class RoomRepositoryTest {
    private RoomRepository roomRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        roomRepository = new RoomRepository(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE History IF EXISTS ");
        jdbcTemplate.execute("CREATE TABLE History (" +
                "                           history_id int not null auto_increment," +
                "                           name varchar(100) not null," +
                "                           is_end boolean not null default false," +
                "                           PRIMARY KEY (history_id))");
        jdbcTemplate.update("INSERT INTO History(name, is_end) VALUES(?, ?)", "joanne", "false");
    }

    @Test
    void insert() {
        final int id = roomRepository.insert("whybe");
        assertThat(id).isEqualTo(2);
    }

    @Test
    void findIdByName() {
        final Optional<Integer> id = roomRepository.findIdByName("joanne");
        assertThat(id).isPresent();
    }

    @Test
    void selectActive() {
        roomRepository.insert("jino");
        roomRepository.insert("pobi");
        roomRepository.insert("jason");
        final List<RoomDto> names = roomRepository.selectWaitRooms();
        assertThat(names.size()).isEqualTo(4);
    }

    @Test
    void updateEndState() {
        roomRepository.updateWaitState(String.valueOf(1));
        final List<RoomDto> names = roomRepository.selectWaitRooms();
        assertThat(names.size()).isEqualTo(0);
    }
}