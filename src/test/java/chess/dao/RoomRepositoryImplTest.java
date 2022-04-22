package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.web.dto.RoomDto;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@JdbcTest
public class RoomRepositoryImplTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;
    private RoomRepository roomRepository;

    @BeforeEach
    void init() {
        roomRepository = new RoomRepositoryImpl(dataSource, jdbcTemplate);
    }

    @Test
    @DisplayName("뱡 insert")
    void insert() {
        int id = roomRepository.save("summer");
        assertThat(id).isGreaterThan(0);
    }

    @Test
    @DisplayName("방 find")
    void find() {
        roomRepository.save("pobi");
        RoomDto room = roomRepository.find("pobi").orElseThrow();
        assertThat(room.getName()).isEqualTo("pobi");
    }
}
