package chess.dto;

public final class GameRoomDto {
    private int gameId;

    private String title;
    private String password;
    public GameRoomDto() {
    }

    public GameRoomDto(String title, int gameId) {
        this.title = title;
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
