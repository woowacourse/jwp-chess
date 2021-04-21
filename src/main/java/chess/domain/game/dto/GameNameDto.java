package chess.domain.game.dto;

public class GameNameDto {

    private String gameName;

    public GameNameDto() {
    }

    public GameNameDto(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
