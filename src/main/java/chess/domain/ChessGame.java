package chess.domain;

import java.util.Objects;

public class ChessGame {

    private static final int MAX_TITLE_LENGTH = 20;

    private long id;
    private final String turn;
    private final String title;
    private final String password;

    public ChessGame(String turn, String title, String password) {
        validateTitleLength(title);
        this.turn = turn;
        this.title = title;
        this.password = password;
    }

    public ChessGame(long id, String turn, String title, String password) {
        this(turn, title, password);
        this.id = id;
    }

    private void validateTitleLength(String title) {
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("체스 게임의 제목은 20자 이하입니다.");
        }
    }

    public long getId() {
        return id;
    }

    public String getTurn() {
        return turn;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return id == chessGame.id && Objects.equals(turn, chessGame.turn) && Objects.equals(title,
                chessGame.title) && Objects.equals(password, chessGame.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, turn, title, password);
    }
}
