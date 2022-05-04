package chess.dto;

public class RoomRequestDto {

    private long id;
    private String title;
    private String password;

    public RoomRequestDto() {
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
