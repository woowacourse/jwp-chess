package chess.domain;

import chess.domain.state.Turn;
import java.util.Objects;

public class ChessGameRoom {

    private final Long id;
    private final String title;
    private final String password;
    private final Turn turn;

    public ChessGameRoom(Long id, String title, String password, Turn turn) {
        Objects.requireNonNull(title, "title은 null이 들어올 수 없습니다.");
        Objects.requireNonNull(password, "password는 null이 들어올 수 없습니다.");
        Objects.requireNonNull(turn, "turn은 null이 들어올 수 없습니다.");
        this.id = id;
        this.title = title;
        this.password = password;
        this.turn = turn;
    }

    public static ChessGameRoom createNewChessGameRoom(String title, String password) {
        return new ChessGameRoom(null, title, password, Turn.WHITE_TURN);
    }

    public void checkCanDelete(String password) {
        checkEndTurn();
        checkPassword(password);
    }

    private void checkEndTurn() {
        if (!turn.isEnd()) {
            throw new IllegalStateException("삭제는 종료상태에서만 할 수 있습니다.");
        }
    }

    private void checkPassword(String password) {
        Objects.requireNonNull(password, "password는 null이 들어올 수 없습니다.");
        if (!this.password.equals(password)) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    public Turn getTurn() {
        return turn;
    }
}
