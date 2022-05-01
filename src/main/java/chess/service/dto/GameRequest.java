package chess.service.dto;

public class GameRequest {

    private Long id;
    private String password;

    public GameRequest() {}

    public GameRequest(Long id, String password) {
        this.id = id;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
