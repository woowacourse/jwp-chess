package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

import chess.domain.ChessGame;
import chess.domain.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GameDaoTest {
    @Autowired
    public RoomDao roomDao;
    @Autowired
    public GameDao gameDao;
    private long roomNo;

    @BeforeEach
    void initializeData() {
        roomNo = roomDao.insert(Room.create("title", "password"));
    }

    @DisplayName("기존에 저장된 game이 있어도 data를 덮어쓸 수 있다.")
    @Test
    void save_twice() {
        gameDao.insert(ChessGame.create(), roomNo);
        assertThatNoException().isThrownBy(() -> gameDao.update(roomNo, true));
    }

    @DisplayName("초기 상태의 게임을 저장하고 불러오면 백색 진영의 차례이다.")
    @Test
    void isWhiteTurn_false() {
        gameDao.insert(ChessGame.create(), roomNo);
        assertThat(gameDao.isWhiteTurn(roomNo)).isTrue();
    }

    @AfterEach
    void delete() {
        gameDao.delete(roomNo);
        roomDao.delete(roomNo);
    }
}
