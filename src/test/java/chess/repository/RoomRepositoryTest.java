package chess.repository;

import chess.dto.web.RoomDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestPropertySource("classpath:application.properties")
class RoomRepositoryTest {

    private final RoomRepository roomRepository;
    private RoomDto roomDto;

    public RoomRepositoryTest(JdbcTemplate jdbcTemplate) {
        roomRepository = new RoomRepository(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        roomDto = new RoomDto("1", "fortuneRoom", "fortune", "portune");
        roomRepository.insert(roomDto);
    }

    @DisplayName("insert를 하면, 방 정보가 테이블에 들어간다.")
    @Test
    void insert() {
        roomRepository.insert(new RoomDto("2", "rootRoom", "root", "loot"));
    }

    @DisplayName("열려있는 방을 조회하면, 열려있는 방들이 반환된다.")
    @Test
    void openedRooms() {
        List<RoomDto> roomDtoList = new ArrayList<>(Collections.singletonList(roomDto));
        assertThat(roomRepository.openedRooms()).usingRecursiveComparison().isEqualTo(roomDtoList);
        roomDtoList.add(new RoomDto("3", "daveRoom", "dave", "davee"));
        assertThat(roomRepository.openedRooms()).usingRecursiveComparison().isNotEqualTo(roomDtoList);
    }

    @DisplayName("방을 닫으면, id에 대응하는 방이 닫힌다.")
    @Test
    void close(){
        assertThat(roomRepository.openedRooms()).isNotEmpty();
        roomRepository.close("1");
        assertThat(roomRepository.openedRooms()).isEmpty();
    }
}