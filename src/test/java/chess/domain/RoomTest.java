package chess.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoomTest {

    @DisplayName("방의 제목을 반환한다.")
    @Test
    void getTitle() {
        final String title = "초보만";
        final String password = "123";
        final Participant participant = new Participant(new Member("one"), new Member("two"));
        final Room room = new Room(title, password, participant);

        assertThat(room.getTitle()).isEqualTo(title);
    }

    @DisplayName("방의 비밀번호를 반환한다.")
    @Test
    void getPassword() {
        final String title = "초보만";
        final String password = "123";
        final Participant participant = new Participant(new Member("one"), new Member("two"));
        final Room room = new Room(title, password, participant);

        assertThat(room.getPassword()).isEqualTo(password);
    }

    @DisplayName("방의 참가자를 반환한다.")
    @Test
    void getParticipant() {
        final String title = "초보만";
        final String password = "123";
        final Participant participant = new Participant(new Member("one"), new Member("two"));
        final Room room = new Room(title, password, participant);

        assertThat(room.getParticipant()).isEqualTo(participant);
    }

}
