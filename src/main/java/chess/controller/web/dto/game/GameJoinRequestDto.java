package chess.controller.web.dto.game;

public class GameJoinRequestDto {

    private Long id;
    private String username;
    private String password;

    public GameJoinRequestDto(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
