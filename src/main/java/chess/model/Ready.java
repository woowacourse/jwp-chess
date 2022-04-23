package chess.model;

public class Ready {
    public Playing start() {
        return new Playing();
    }

    public Playing move() {
        throw new IllegalArgumentException("게임이 시작되지 않았으면 움직일 수 없습니다.");
    }

    public Playing status() {
        throw new IllegalArgumentException("게임이 시작되지 않았으면 점수를 조회할 수 없습니다.");
    }

    public End end() {
        throw new IllegalArgumentException("게임이 시작되지 않았으면 게임을 종료할 수 없습니다.");
    }
}
