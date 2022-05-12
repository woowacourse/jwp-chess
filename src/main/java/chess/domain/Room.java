package chess.domain;

public class Room {

    private String name;
    private String password;

    public Room(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public void validatePassword(String inputPassword) {
        if (!this.password.equals(inputPassword)) {
            throw new IllegalArgumentException("[ERROR] 입력한 비밀번호가 일치하지 않습니다.");
        }
    }
}
