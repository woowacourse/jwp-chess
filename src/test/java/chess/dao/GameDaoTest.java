package chess.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class GameDaoTest {
    @Autowired
    private GameDao gameDao;

    @BeforeEach
    public void setup() {
    }

    @Test
    void insert() {
//        assertThat(gameDao.insert("asd")).isNotEqualTo(0L);
    }

    @Test
    void delete() {
//        gameDao.delete(1L);
    }

}