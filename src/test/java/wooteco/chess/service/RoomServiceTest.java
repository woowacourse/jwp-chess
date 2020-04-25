package wooteco.chess.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wooteco.chess.dto.RoomDto;
import wooteco.chess.repository.CachedRoomRepository;
import wooteco.chess.repository.MoveRepository;
import wooteco.chess.repository.MoveRepositoryImpl;
import wooteco.chess.repository.RoomRepository;
import wooteco.chess.utils.IdGenerator;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class RoomServiceTest {
    private RoomService roomService;

    @BeforeEach
    void setup() {
        roomService = new RoomService();
    }

    @AfterEach
    void tearDown() throws SQLException {
        MoveRepository moveRepository = new MoveRepositoryImpl();
        moveRepository.deleteAll();
        RoomRepository roomRepository = new CachedRoomRepository();
        roomRepository.deleteAll();
    }

    @Test
    void create() {
        RoomDto roomDto = new RoomDto();
        assertThat((int) roomService.create(roomDto).getObject())
                .isEqualTo(IdGenerator.generateRoomId() - 1);
    }
}
