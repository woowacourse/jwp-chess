package chess.dto;

public class RoomDto {
    private String name;
    private String pw;

    public RoomDto() {
    }

    public RoomDto(String name, String pw) {
        this.name = name;
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public String getPw() {
        return pw;
    }
}
