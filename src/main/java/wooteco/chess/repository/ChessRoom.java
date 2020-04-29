package wooteco.chess.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("ChessRoom")
public class ChessRoom {
    @Id
    @Column("RoomID")
    private Long roomId;

    @Column("RoomName")
    private String roomName;

    public ChessRoom() {
    }

    public ChessRoom(String roomName) {
        this.roomName = roomName;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }
}
