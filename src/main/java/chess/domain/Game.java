package chess.domain;

public class Game {

    private int id;
    private String password;

    public Game(int id, String password) {
        this.id = id;
        this.password = password;
    }

    public void matchesPassword(final String password) {
        if (!this.password.equals(password)) {
            throw new IllegalArgumentException("비밀번호가 잘못되었습니다.");
        }
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}
