package chess.dto;

public class CreateBoardDto {
    private final String name;
    private final String password;

    public CreateBoardDto(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
