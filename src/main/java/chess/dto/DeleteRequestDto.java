package chess.dto;

public class DeleteRequestDto {
    private int id;
    private String password;

    public DeleteRequestDto(int id, String password) {
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
