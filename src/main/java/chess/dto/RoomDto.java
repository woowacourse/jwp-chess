package chess.dto;

import chess.domain.room.Room;

public class RoomDto {

    private long id;
    private String title;
    private String password;

    public RoomDto() {
    }

    public static RoomDto toDto(Room room) {
        return new RoomDto(room.getId(), room.getTitle());
    }

    public RoomDto(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public RoomDto(String title, String password) {
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
