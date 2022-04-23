package chess.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReadyTest {

    @Test
    @DisplayName("대기 상황에서 시작 상태 만들기")
    void start() {
        Ready ready = new Ready();
        Playing playing = ready.start();
        assertThat(playing.isPlaying()).isTrue();
    }

    @Test
    @DisplayName("대기 상황에서 움직일 경우 예외")
    void move() {
        Ready ready = new Ready();
        assertThatThrownBy(() -> {
            Playing playing = ready.move();
        }).isInstanceOf(RuntimeException.class)
                .hasMessage("게임이 시작되지 않았으면 움직일 수 없습니다.");
    }

    @Test
    @DisplayName("대기 상황에서 점수 조회시 예외")
    void status() {
        Ready ready = new Ready();
        assertThatThrownBy(() -> {
            Playing playing = ready.status();
        }).isInstanceOf(RuntimeException.class)
                .hasMessage("게임이 시작되지 않았으면 점수를 조회할 수 없습니다.");
    }

    @Test
    @DisplayName("대기 상황에서 종료하면 예외")
    void end() {
        Ready ready = new Ready();
        assertThatThrownBy(() -> {
            End end = ready.end();
        }).isInstanceOf(RuntimeException.class)
                .hasMessage("게임이 시작되지 않았으면 게임을 종료할 수 없습니다.");
    }
}
