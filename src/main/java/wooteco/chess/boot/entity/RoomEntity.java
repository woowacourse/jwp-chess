package wooteco.chess.boot.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("room")
public class RoomEntity {

    @Id
    private Long id;
    private Long roomNumber;

    public RoomEntity(Long roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Long getId() {
        return id;
    }

    public Long getRoomNumber() {
        return roomNumber;
    }
}
