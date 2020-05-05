package wooteco.chess.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.chess.domain.coordinate.Coordinate;
import wooteco.chess.dto.RequestDto.ChessMoveRequestDto;
import wooteco.chess.dto.RequestDto.RoomCreateRequestDto;
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
        RoomCreateRequestDto requestDto = new RoomCreateRequestDto("whitePassword",
                "renewTest");
        Long roomId = (Long)roomService.create(requestDto)
                .getResponseData();
        assertThat(chessService.renew(roomId).getResponseCode()).isEqualTo(200);
    }

    @DisplayName("getMovableWay 테스트")
    @Test
    void getMovableWay() {
        RoomCreateRequestDto requestDto = new RoomCreateRequestDto("whitePassword",
                "wayTest");
        Long roomId = (Long)roomService.create(requestDto)
                .getResponseData();
        chessService.renew(roomId);
        assertThat(chessService.getMovableWay(roomId, Coordinate.of("a2"),"whitePassword")
                .getResponseCode())
                .isEqualTo(200);
    }

    @DisplayName("move 테스트")
    @Test
    void move() {
        RoomCreateRequestDto requestDto = new RoomCreateRequestDto("whitePassword",
                "moveTest");
        Long roomId = (Long)roomService.create(requestDto)
                .getResponseData();
        chessService.renew(roomId);
        ChessMoveRequestDto moveRequestDto = new ChessMoveRequestDto(roomId,"whitePassword", "a2", "a4");
        assertThat(chessService.move(moveRequestDto)
                .getResponseCode())
                .isEqualTo(200);
    }
}
