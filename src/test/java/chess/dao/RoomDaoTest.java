package chess.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class RoomDaoTest {
    @Autowired
    private RoomDao roomDao;

    @BeforeEach
    public void setup() {
    }

    @Test
    void insert() {
        assertThat(roomDao.insert("asd")).isNotEqualTo(0L);
    }

    @Test
    void delete() {
        roomDao.delete(1L);
    }

}