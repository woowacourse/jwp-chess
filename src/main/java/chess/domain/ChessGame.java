package chess.domain;

import chess.domain.state.ChessGameState;
import java.util.Objects;

public class ChessGame {

    private final long id;
    private final String title;
    private final String password;
    private final ChessGameState chessGameState;

    public ChessGame(long id, String title, String password, ChessGameState chessGameState) {
        Objects.requireNonNull(title, "title은 null이 들어올 수 없습니다.");
        Objects.requireNonNull(password, "password는 null이 들어올 수 없습니다.");
        Objects.requireNonNull(chessGameState, "chessgameState는 null이 들어올 수 없습니다.");
        this.id = id;
        this.title = title;
        this.password = password;
        this.chessGameState = chessGameState;
    }

    public void checkPassword(String password) {
        if (!this.password.equals(password)) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
    }
}
