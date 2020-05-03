package wooteco.chess.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJdbcTest
@Transactional
public class RoomInfoRepositoryTest {

    @Autowired
    private RoomInfoRepository roomInfoRepository;

    @DisplayName("체스 말 정보 저장")
    @Test
    void save() {
        RoomInfo roomInfo = roomInfoRepository.save(new RoomInfo("hi", "white", false));
        RoomInfo foundRoomInfo = roomInfoRepository.findByRoomNameOnlyOne("hi");
        assertThat(foundRoomInfo.getRoomName()).isEqualTo("hi");
    }
}
