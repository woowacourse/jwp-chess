package wooteco.chess.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.chess.repository.RoomEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class BoardServiceTest {

    @Autowired
    private BoardService boardService;
    @Autowired
    private RoomService roomService;

    private long testId1;
    private long testId2;

    @BeforeEach
    void setUp(){
        roomService.createRoom("room1");
        roomService.createRoom("room2");
        List<RoomEntity> roomEntities = roomService.findAllRoom();
        testId2 = roomEntities.get(roomEntities.size()-1).getId();
        testId1 = roomEntities.get(roomEntities.size()-2).getId();
        roomService.initializeBoard(testId1);
        roomService.initializeBoard(testId2);
    }

    @Test
    @DisplayName("각 게임별로 말이 독립적으로 움직이는지 확인")
    void moveTest(){
        assertThat(boardService.movePiece(testId1,"a2","a3")).isNotNull();
        assertThat(boardService.movePiece(testId2,"a2","a3")).isNotNull();
    }

    @Test
    @DisplayName("점수값이 잘 들어왔지는 확인")
    void isFinishTest(){
        Map<String, String> model = boardService.showScoreStatus(testId1);
        assertThat(model.size()).isEqualTo(2);
        assertThat(model.get("black")).isNotNull();
        assertThat(model.get("white")).isNotNull();
    }
}
