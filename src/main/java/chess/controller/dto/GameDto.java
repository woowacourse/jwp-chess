package chess.controller.dto;

public class GameDto {
    private Long id;
    private String gameName;

    public GameDto(String gameName) {
        this.gameName = gameName;
    }

    public GameDto(Long id, String gameName) {
        this.id = id;
        this.gameName = gameName;
    }

    public Long getId() {
        return id;
    }

    public String getGameName() {
        return gameName;
    }
}
