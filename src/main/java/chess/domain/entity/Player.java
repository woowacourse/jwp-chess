package chess.domain.entity;

public class Player {
    private int id;
    private String nickname;
    private String password;

    public Player(final int id, final String nickname, final String password) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
    }

    public Player(final int id, final String nickname) {
        this(id, nickname, null);
    }

    public Player(final int id) {
        this(id, null, null);
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }
}
