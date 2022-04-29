package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@TestPropertySource(properties = "spring.config.location=classpath:application-test.yml" )
@SpringBootTest
@Transactional
class RoomImplDaoTest {

    @Autowired
    private RoomImplDao roomDao;

    @Test
    @DisplayName("한개의 방을 생성할 수 있다.")
    void insertRoom() {
        Long roomId = roomDao.insertRoom("title1", "1111");

        assertThat(roomId).isInstanceOf(Long.class);
    }
}
