package chess.dto;

public class RoomDto {

    private String title;
    private String password;

    private int id;

    public RoomDto() {
    }

    public RoomDto(String title, String password, int id) {
        this.title = title;
        this.password = password;
        this.id = id;
    }

    public RoomDto(String title, String password) {
        this(title, password, 0);
    }

    public RoomDto(String title, int id) {
        this(title, "", id);
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }
}
