package chess.dto;

public class RoomResponseDto {

    private String id;
    private String name;
    private String pw;

    public RoomResponseDto(String id, String name, String pw) {
        this.id = id;
        this.name = name;
        this.pw = pw;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPw() {
        return pw;
    }
}
