package chess.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import chess.domain.piece.Color;
import chess.domain.room.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;


@JdbcTest
public class GameDaoTest {

    private GameDao gameDao;
    private long id;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameDao = new GameDao(jdbcTemplate);
        id = gameDao.createByTitleAndPassword(new Room("게임방제목", "password486"));
    }

    @DisplayName("게임을 만들 때 title과 password를 입력받는다.")
    @Test
    void createById() {
        long gameId = gameDao.createByTitleAndPassword(new Room("게임방입니다~", "password486"));
        assertThat(gameDao.exists(String.valueOf(gameId))).isTrue();
    }

    @DisplayName("게임이 끝나지 않은 경우 end_flag는 false이다.")
    @Test
    void findEndFlagById_false() {
        assertThat(gameDao.findRoomById(id).getEndFlag()).isFalse();
    }

    @DisplayName("turn칼럼에는 불가능한 turn이 저장되며 초깃값은 black이다.")
    @Test
    void findTurnById() {
        assertThat(gameDao.findRoomById(id).getTurn()).isEqualTo(Color.BLACK);
    }

    @DisplayName("turn 업데이트 성공")
    @Test
    void updateTurnById() {
        gameDao.updateTurnById(Color.WHITE, id);

        assertThat(gameDao.findRoomById(id).getTurn()).isEqualTo(Color.WHITE);
    }

    @DisplayName("end_flag 업데이트 성공")
    @Test
    void updateEndFlagById() {
        gameDao.updateEndFlagById(true, id);

        assertThat(gameDao.findRoomById(id).getEndFlag()).isTrue();
    }

    @DisplayName("게임 삭제시 더이상 게임은 존재하지 않는다.")
    @Test
    void deleteById_게임_삭제_성공() {
        gameDao.deleteById(id);

        assertThat(gameDao.exists("1234")).isFalse();
    }

}
