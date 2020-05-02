package wooteco.chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("room")
public class Room {
    public static final Long DEFAULT_USER_ID = -1L;
    public static final String DEFAULT_NAME = "default";

    @Id
    private Long id;
    private Long blackUserId;
    private Long whiteUserId;
    private boolean isEnd;
    private String name;

    public Room(Long id, Long blackUserId, Long whiteUserId, boolean isEnd, String name) {
        this.id = id;
        this.blackUserId = blackUserId;
        this.whiteUserId = whiteUserId;
        this.isEnd = isEnd;
        this.name = name;
    }

    public Room(Long blackUserId, Long whiteUserId, boolean isEnd, String name) {
        this.blackUserId = blackUserId;
        this.whiteUserId = whiteUserId;
        this.isEnd = isEnd;
        this.name = name;
    }


    public Room() {
        this(DEFAULT_USER_ID, DEFAULT_USER_ID, false, DEFAULT_NAME);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBlackUserId() {
        return blackUserId;
    }

    public void setBlackUserId(Long blackUserId) {
        this.blackUserId = blackUserId;
    }

    public Long getWhiteUserId() {
        return whiteUserId;
    }

    public void setWhiteUserId(Long whiteUserId) {
        this.whiteUserId = whiteUserId;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }
}
