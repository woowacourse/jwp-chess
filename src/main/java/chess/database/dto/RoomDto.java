package chess.database.dto;

public class RoomDto {

    private int id;
    private String name;
    private String password;

    public RoomDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public RoomDto(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public RoomDto(int id, String name, String password) {
        this(name, password);
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
