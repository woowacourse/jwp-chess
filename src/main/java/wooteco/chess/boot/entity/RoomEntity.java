package wooteco.chess.boot.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Table("room")
public class RoomEntity {

    @Id
    private Long id;
    private Long roomNumber;

    public RoomEntity(Long roomNumber) {
        this.roomNumber = roomNumber;
    }

    //임시로 방 하나만 쓰기 위한 코드(4단계에서 수정)
    @PersistenceConstructor
    public RoomEntity(Long id, Long roomNumber) {
        this.id = id;
        this.roomNumber = roomNumber;
    }

    public Long getId() {
        return id;
    }

    public Long getRoomNumber() {
        return roomNumber;
    }
}
