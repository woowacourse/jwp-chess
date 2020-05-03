package wooteco.chess.boot.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Table("room_info")
public class RoomInfoEntity {
    @Id
    private Long id;
    private Long roomId;
    private String turn;
    private boolean isOver;

    public RoomInfoEntity(Long roomId, String turn, boolean isOver) {
        this.roomId = roomId;
        this.turn = turn;
        this.isOver = isOver;
    }

    @PersistenceConstructor
    public RoomInfoEntity(Long id, Long roomId, String turn, boolean isOver) {
        this.id = id;
        this.roomId = roomId;
        this.turn = turn;
        this.isOver = isOver;
    }

    private Long getRoomId() {
        return roomId;
    }

    public String getTurn() {
        return turn;
    }

    private boolean isOver() {
        return isOver;
    }
}
