package wooteco.chess.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.entity.Room;
import wooteco.chess.repository.RoomRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class roomServiceTest {
    @Autowired
    RoomService roomService;
    @Autowired
    RoomRepository roomRepository;

    @AfterEach
    void deleteAll() {
        roomRepository.deleteAll();
    }

    @DisplayName("create 테스트")
    @Test
    void create() {
        Room room = new Room("blackPassword",
                "whitePassword",
                false,
                "createTest");
        assertThat(roomService.create(room)
                .getResponseCode())
                .isEqualTo(200);
    }

    @DisplayName("join 테스트")
    @Test
    void join() {
        Room room = new Room("default",
                "whitePassword",
                false,
                "joinTest");
        roomService.create(room);
        assertThat(roomService.join("joinTest", "blackPassword")
                .getResponseCode())
                .isEqualTo(200);
    }

    @DisplayName("checkAuthentication 테스트")
    @Test
    void checkAuthentication() {
        Room room = new Room("default",
                "whitePassword",
                false,
                "authenticationTest");
        room = (Room)roomService.create(room).getResponseData();
        assertThat(roomService.checkAuthentication(room.getId(), "whitePassword"))
                .isEqualTo(Team.WHITE);
    }
}
