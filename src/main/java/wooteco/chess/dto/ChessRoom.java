package wooteco.chess.dto;

import org.springframework.data.annotation.Id;

public class ChessRoom {
    @Id
    private Long id;
    private String roomName;

    public ChessRoom() {
    }

    public ChessRoom(Long id, String roomName) {
        this.id = id;
        this.roomName = roomName;
    }

    public Long getId() {
        return id;
    }

    public String getRoomName() {
        return roomName;
    }
}
