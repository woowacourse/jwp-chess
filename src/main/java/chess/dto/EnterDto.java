package chess.dto;

public class EnterDto {

    private Long id;
    private String password;

    public EnterDto() {
    }

    public EnterDto(Long id, String password) {
        this.id = id;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}
