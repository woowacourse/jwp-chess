package chess.dto;

public class RoomDto {

    private int id;
    private String title;
    private String password;


    public RoomDto() {
    }

    public RoomDto(int id, String title, String password) {
        this.id = id;
        this.title = title;
        this.password = password;
    }

    public RoomDto(String title, String password) {
        this(0, title, password);
    }

    public RoomDto(int id, String title) {
        this(id, title, "");
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
