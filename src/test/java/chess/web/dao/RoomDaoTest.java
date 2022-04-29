package chess.web.dao;

import chess.domain.board.Team;
import chess.domain.board.Turn;
import chess.domain.entity.Room;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
@Sql("/data.sql")
class RoomDaoTest {

    @Autowired
    private RoomDao roomDao;

    private final Long boardId = 1L;
    private static final String TITLE = "제목";

    @Test
    @DisplayName("처음 저장된 board가 초기화된 Turn과 동일한지 테스트")
    void findTurnById() {
        //when
        Turn turn = roomDao.findTurnById(boardId).get();
        //then
        assertThat(turn).isEqualTo(Turn.init());
    }

    @Test
    @DisplayName("현재 턴을 black으로 업데이트하면 DB에 반영이 되는지 테스트")
    void updateTurnById() {
        //when
        roomDao.updateTurnById(boardId, "black");
        Turn turn = roomDao.findTurnById(boardId).get();
        //then
        assertThat(turn).isEqualTo(new Turn(Team.BLACK));
    }

    @Test
    @DisplayName("새로운 보드판이 만들어지면 pk값이 1 증가한다.")
    void save() {
        //when
        Long saveId = roomDao.save("방제목", "123");
        //then
        assertThat(boardId + 1).isEqualTo(saveId);
        roomDao.deleteById(saveId);
    }

    @Test
    @DisplayName("해당 식별자의 방 정보를 가져온다.")
    void findById() {
        Room room = roomDao.findById(boardId).get();
        assertAll(
                () -> assertThat(room.getId()).isEqualTo(boardId),
                () -> assertThat(room.getTitle()).isEqualTo(TITLE),
                () -> assertThat(room.getPassword()).isEqualTo("패스워드")
        );
    }

    @Test
    @DisplayName("식별자를 통해 방을 제거하면 남은 방 개수는 이전보다 한 개 적어야한다.")
    void deleteById() {
        int before = roomDao.findAll().size();
        roomDao.deleteById(boardId);
        int after = roomDao.findAll().size();
        assertThat(after).isEqualTo(before - 1);
    }

    @Test
    @DisplayName("전체 방 개수인 한 개가 나온다.")
    void findAll() {
        assertThat(roomDao.findAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("이미 존재하는 제목이 있다면 true를 반환한다.")
    void existByTitle() {
        assertThat(roomDao.existByTitle(TITLE)).isTrue();
    }

}
