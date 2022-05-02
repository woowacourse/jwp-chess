package chess.dto.request;

public class DeleteGameDto {

    private final int id;
    private final String password;

    public DeleteGameDto(int id, String password) {
        this.id = id;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}
