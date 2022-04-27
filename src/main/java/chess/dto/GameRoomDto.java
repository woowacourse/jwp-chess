package chess.dto;

public class GameRoomDto {

    private long id;
    private String title;
    private String password;

    public GameRoomDto() {
    }

    public GameRoomDto(String title, String password) {
        this.title = title;
        this.password = password;
    }

    public GameRoomDto(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public GameRoomDto(long id, String title, String password) {
        this.id = id;
        this.title = title;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
