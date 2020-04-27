package wooteco.chess.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import wooteco.chess.domain.Color;

@Table("room")
public class RoomEntity {

    @Id
    private Integer roomId;

    private String roomName;

    @Column("room_color")
    private String currentColor;


    private RoomEntity(){
    }

    public RoomEntity(final String roomName, final String currentColor) {
        this.roomName = roomName;
        this.currentColor = currentColor;
    }

    public RoomEntity(final int roomId, final String roomName, final String currentColor) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.currentColor = currentColor;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getCurrentColor() {
        return currentColor;
    }
}
