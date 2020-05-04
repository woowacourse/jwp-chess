package wooteco.chess.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJdbcTest
public class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    void setUp(){
        roomRepository.save(new RoomEntity("테스트 방 1입니다."));
    }

    @Test
    @DisplayName("방 저장 확인")
    void saveTest(){
        roomRepository.save(new RoomEntity("테스트 방 2입니다."));
        assertThat(roomRepository.findById((long) 2)).isNotNull();
        System.out.println(roomRepository.findAll().size());
        assertThat(roomRepository.findAll().size()).isEqualTo(2);
    }
}
