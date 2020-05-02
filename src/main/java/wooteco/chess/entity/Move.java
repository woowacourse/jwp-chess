package wooteco.chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import wooteco.chess.domain.coordinate.Coordinate;

import java.util.Objects;

@Table("move")
public class Move {
    @Id
    private Long id;
    private Long roomId;
    private String source;
    private String target;

    public Move(Long id, Long roomId, String source, String target) {
        this.id = id;
        this.roomId = roomId;
        this.source = source;
        this.target = target;
    }

    public Move(Long roomId, String source, String target) {
        this.roomId = roomId;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
