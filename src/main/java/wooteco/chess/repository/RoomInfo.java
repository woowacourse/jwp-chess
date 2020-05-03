package wooteco.chess.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("room_info")
public class RoomInfo {

    @Id
    private Long id;
    private String roomName;
    private String currentTurn;
    private boolean isGameEnd;

    public RoomInfo() {
    }

    public RoomInfo(String roomName, String currentTurn, boolean isGameEnd) {
        this.roomName = roomName;
        this.currentTurn = currentTurn;
        this.isGameEnd = isGameEnd;
    }

    public Long getId() {
        return this.id;
    }

    public String getRoomName() {
        return this.roomName;
    }

    public String getCurrentTurn() {
        return currentTurn;
    }
}
