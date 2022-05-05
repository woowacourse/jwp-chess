package chess.repository;

import static org.assertj.core.api.Assertions.*;

import chess.dao.FakeRoomDao;
import chess.dto.RoomDto;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoomRepositoryTest {
    private RoomRepository roomRepository;

    @BeforeEach
    void setUp() {
        roomRepository = new RoomRepository(new FakeRoomDao());
        roomRepository.save(new RoomDto(1, "chess", "0000", "STOP"));
    }

    @AfterEach
    void clean() {
        roomRepository.deleteRoom(1);
    }

    @Test
    void findAllTest() {
        List<RoomDto> roomDtos = roomRepository.findAll();
        assertThat(roomDtos.size()).isEqualTo(1);
    }

    @Test
    void findPasswordByIdTest() {
        String password = roomRepository.findPasswordById(1);
        assertThat(password).isEqualTo("0000");
    }

    @Test
    void updateStatusTest() {
        roomRepository.updateStatus(1, "PLAY");
        RoomDto roomDto = roomRepository.findAll().get(0);
        assertThat(roomDto.getStatus()).isEqualTo("PLAY");
    }
}
