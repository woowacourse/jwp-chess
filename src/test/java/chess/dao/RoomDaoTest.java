package chess.dao;

import chess.domain.player.Team;
import chess.dto.GameNameAndTurnDto;
import chess.dto.RoomInfoDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class RoomDaoTest {

    @Autowired
    private RoomDao roomDao;

    private final String roomName = "rex_game";
    private final String password = "111";
    private final String turn = Team.WHITE.getName();
    private final long roomId = 1;

    @Test
    @DisplayName("게임방 아이디를 올바르게 찾아온다.")
    void findChessGameIdByName() {
        int id = roomDao.findRoomIdByName(roomName).get();

        assertThat(id).isEqualTo(roomId);
    }

    @Test
    @DisplayName("게임방 아이디를 통해 방 이름과 턴 정보를 올바르게 찾아온다.")
    void findNameAndTurnById() {
        GameNameAndTurnDto result = roomDao.findNameAndTurnById(roomId);

        Assertions.assertAll(
                () -> assertThat(result.getName()).isEqualTo(roomName),
                () -> assertThat(result.getTurn()).isEqualTo(turn)
        );
    }

    @Test
    @DisplayName("방 아이디를 통해 비밀번호를 올바르게 찾아온다.")
    void findRoomPasswordById() {
        String result = roomDao.findRoomPasswordById(roomId);

        assertThat(result).isEqualTo(password);
    }

    @Test
    @DisplayName("모든 방 정보를 올바르게 찾아온다.")
    void findAll() {
        List<RoomInfoDto> result = roomDao.findAll();

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("게임을 올바르게 저장한다.")
    void saveChessGame() {
        String newGameName = "new Game";
        String password = "password";
        Team turn = Team.WHITE;

        roomDao.insert(newGameName, password, turn);
        Optional<Integer> id = roomDao.findRoomIdByName(newGameName);

        assertThat(id.isPresent()).isTrue();
    }

    @Test
    @DisplayName("게임방 아이디를 통해 현재 턴을 찾아온다.")
    void findCurrentTurn() {
        String currentTurn = roomDao.findTurn(roomId);
        String expected = Team.WHITE.getName();

        assertThat(currentTurn).isEqualTo(expected);
    }

    @Test
    @DisplayName("턴 정보를 업데이트 한다.")
    void updateGameTurn() {
        Team nextTurn = Team.BLACK;
        roomDao.updateTurn(roomId, nextTurn);

        String currentTurn = roomDao.findTurn(roomId);

        assertThat(currentTurn).isEqualTo(nextTurn.getName());
    }

    @Test
    @DisplayName("게임을 삭제한다.")
    void deleteChessGame() {
        roomDao.delete(roomId);

        boolean result = roomDao.findRoomIdByName(roomName).isPresent();

        assertThat(result).isFalse();
    }
}
