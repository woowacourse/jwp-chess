package wooteco.chess.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.chess.domain.coordinate.Coordinate;
import wooteco.chess.entity.Move;
import wooteco.chess.entity.Room;
import wooteco.chess.repository.MoveRepository;
import wooteco.chess.repository.RoomRepository;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class chessServiceTest {
    @Autowired
    ChessService chessService;
    @Autowired
    RoomService roomService;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    MoveRepository moveRepository;

    @AfterEach
    void deleteAll() {
        moveRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @DisplayName("renew 테스트")
    @Test
    void renew() {
        Room room = (Room)roomService.create(
                new Room("blackPassword", "whitePassword",
                        false,"renewTest"))
                .getResponseData();
        assertThat(chessService.renew(room.getId()).getResponseCode()).isEqualTo(200);
    }

    @DisplayName("getMovableWay 테스트")
    @Test
    void getMovableWay() {
        Room room = (Room)roomService.create(
                new Room("blackPassword", "whitePassword",
                        false,"wayTest"))
                .getResponseData();
        chessService.renew(room.getId());
        assertThat(chessService.getMovableWay(room.getId(), Coordinate.of("a2"),"whitePassword")
                .getResponseCode())
                .isEqualTo(200);
    }

    @DisplayName("move 테스트")
    @Test
    void move() {
        Room room = (Room)roomService.create(
                new Room("blackPassword", "whitePassword",
                        false,"wayTest"))
                .getResponseData();
        chessService.renew(room.getId());
        assertThat(chessService.move(new Move(room.getId(), "a2", "a4"),"whitePassword")
                .getResponseCode())
                .isEqualTo(200);
    }
}
