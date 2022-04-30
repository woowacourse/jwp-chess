package chess.dto;

public class LogInDto {
    private final String id;
    private final String password;

    public LogInDto(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "LogInDto{" +
                "gameId='" + id + '\'' +
                ", gamePassword='" + password + '\'' +
                '}';
    }
}
