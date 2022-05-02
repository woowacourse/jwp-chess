package chess.web.dto;

public class DeleteDto {
    private String password;

    public DeleteDto() {
    }

    public DeleteDto(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
