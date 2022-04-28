package chess.dto.request;

public class CreateRoomDto {
    private final String name;
    private final String password;

    public CreateRoomDto(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "CreateRoomDto{" +
            "name='" + name + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
