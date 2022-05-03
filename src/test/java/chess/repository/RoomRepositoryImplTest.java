package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.entity.RoomEntity;
import chess.util.PasswordSha256Encoder;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@ActiveProfiles("test")
@JdbcTest
@Transactional
class RoomRepositoryImplTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private RoomRepository roomRepository;

    @BeforeEach
    void setUp() {
        roomRepository = new RoomRepositoryImpl(jdbcTemplate, dataSource);
    }

    @DisplayName("전체 룸을 가져온다.")
    @Test
    void findRooms() {
        final List<RoomEntity> rooms = roomRepository.findRooms();
        assertThat(rooms).isEmpty();
    }

    @DisplayName("룸을 생성한다.")
    @Test
    void insert() {
        final RoomEntity roomEntity = new RoomEntity("1234", "체스 초보만", "white", false);
        final RoomEntity insertRoom = roomRepository.insert(roomEntity);
        assertThat(insertRoom.getName()).isEqualTo(roomEntity.getName());
    }

    @DisplayName("중복된 룸을 생성하면, DuplicateKeyException 예외가 발생한다.")
    @Test
    void insert_duplicate() {
        final RoomEntity roomEntity = new RoomEntity("1234", "체스 초보만", "white", false);
        roomRepository.insert(roomEntity);

        assertThatThrownBy(
            () -> roomRepository.insert(roomEntity)
        ).isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("룸을 조회한다.")
    @Test
    void findById() {
        final RoomEntity roomEntity = new RoomEntity("1234", "체스 초보만", "white", false);
        final RoomEntity insertRoom = roomRepository.insert(roomEntity);

        final RoomEntity targetRoom = roomRepository.findById(insertRoom.getId());
        assertThat(targetRoom.getId()).isEqualTo(insertRoom.getId());
    }

    @DisplayName("룸의 현재 차례를 업데이트한다.")
    @Test
    void updateTeam() {
        final RoomEntity roomEntity = new RoomEntity("1234", "체스 초보만", "white", false);
        final Long id = roomRepository.insert(roomEntity).getId();
        roomRepository.updateTeam(id, "black");

        assertThat(roomRepository.findById(id).getTeam()).isEqualTo("black");
    }

    @DisplayName("룸의 상태를 종료로 변경한다")
    @Test
    void finishRoom() {
        final RoomEntity roomEntity = new RoomEntity("1234", "체스 초보만", "white", false);
        final Long id = roomRepository.insert(roomEntity).getId();
        roomRepository.updateGameOver(id);

        assertThat(roomRepository.findById(id).isGameOver()).isTrue();
    }


    @DisplayName("입력된 룸의 상태로 룸의 정보를 변경한다.")
    @Test
    void update() {
        final RoomEntity roomEntity = new RoomEntity(PasswordSha256Encoder.encode("1234"), "체스 초보만", "white", false);
        final RoomEntity targetRoom = roomRepository.insert(roomEntity);
        final RoomEntity nameChangedRoom = new RoomEntity(null, PasswordSha256Encoder.encode("1234"), "체스 고수만", null,
            null);
        targetRoom.update(nameChangedRoom);
        roomRepository.update(targetRoom);

        assertThat(roomRepository.findById(targetRoom.getId()).getName()).isEqualTo("체스 고수만");
    }
}
