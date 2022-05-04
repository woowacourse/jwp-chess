package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;
import chess.entity.RoomEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql({"/schema.sql"})
class RoomDaoTest {

    @Autowired
    private RoomDao roomDao;

    @Test
    @DisplayName("이름과 비밀번호가 주어지면 새로운 방을 생성한다.")
    void save() {
        // given
        final GameStatus gameStatus = GameStatus.READY;
        final Color currentTurn = Color.WHITE;
        final String password = "1q2w3e4r";

        // when
        final int roomId = roomDao.save(new RoomEntity("hi", gameStatus, currentTurn, password));
        final int roomId2 = roomDao.save(new RoomEntity("hello", gameStatus, currentTurn, password));

        // then
        assertThat(roomId).isEqualTo(1);
        assertThat(roomId2).isEqualTo(2);
    }

    @Test
    @DisplayName("방 id로 방을 삭제한다.")
    void deleteById() {
        // given
        final int roomId = createRoom();

        // when
        final int deletedRow = roomDao.deleteById(roomId);

        // then
        assertThat(deletedRow).isEqualTo(1);
    }

    @Test
    @DisplayName("방 id로 정보를 갱신한다.")
    void updateById() {
        // given
        final GameStatus updatedStatus = GameStatus.PLAYING;
        final Color updatedCurrentTurn = Color.BLACK;

        final int roomId = createRoom();

        // when
        final int updatedRow = roomDao.updateById(roomId, updatedStatus, updatedCurrentTurn);
        final RoomEntity roomEntity = roomDao.findById(roomId);
        final GameStatus actualStatus = roomEntity.toGameStatus();

        // then
        assertThat(updatedRow).isEqualTo(1);
        assertThat(actualStatus).isEqualTo(updatedStatus);
    }

    private int createRoom() {
        final String roomName = "hi";
        final GameStatus gameStatus = GameStatus.READY;
        final Color currentTurn = Color.WHITE;
        final String password = "1q2w3e4r";

        return roomDao.save(new RoomEntity(roomName, gameStatus, currentTurn, password));
    }
}
