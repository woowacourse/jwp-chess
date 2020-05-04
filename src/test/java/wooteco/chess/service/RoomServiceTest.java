package wooteco.chess.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.repository.RoomEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RoomServiceTest {

    @Autowired
    private RoomService roomService;


    @Test
    @DisplayName("보드 초기화 테스트")
    void initBoardTest() {
        assertThat(roomService.initializeBoard((long)1).size()).isEqualTo(64);
    }

    @Test
    @DisplayName("방 새로 만들기 테스트")
    void createRoomTest(){
        roomService.createRoom("testTitle");
        List<RoomEntity> roomEntities = roomService.findAllRoom();
        long testId = roomEntities.get(roomEntities.size()-1).getId();
        assertThat(roomService.findTitleById(testId)).isEqualTo("testTitle");
        assertThat(roomService.findTurnById(testId)).isEqualTo(Team.WHITE.name());
    }

    @Test
    @DisplayName("턴 바꾸기 테스트")
    void changeTurnTest(){
        List<RoomEntity> roomEntities = roomService.findAllRoom();
        long testId = roomEntities.get(0).getId();
        String turn = roomService.findTurnById((testId));
        roomService.updateTurn(testId);
        assertThat(roomService.findTurnById(testId)).isNotEqualTo(turn);
    }

    @Test
    @DisplayName("방 리셋 테스트")
    void resetRoomTest(){
        roomService.createRoom("testTitle");
        List<RoomEntity> roomEntities = roomService.findAllRoom();
        long testId = roomEntities.get(roomEntities.size()-1).getId();
        roomService.updateTurn(testId);     //turn = black
        String turn = roomService.findTurnById((testId));
        roomService.resetRoom(testId);
        assertThat(roomService.findTurnById(testId)).isNotEqualTo(turn);
    }
}
