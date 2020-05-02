package wooteco.chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("room")
public class Room {
    public static final String DEFAULT_USER_ID = "default";
    public static final String DEFAULT_NAME = "default";

    @Id
    private Long id;
    private String blackPassword;
    private String whitePassword;
    private boolean isEnd;
    private String name;

    public Room(Long id, String blackPassword, String whitePassword, boolean isEnd, String name) {
        this.id = id;
        this.blackPassword = blackPassword;
        this.whitePassword = whitePassword;
        this.isEnd = isEnd;
        this.name = name;
    }

    public Room(String blackPassword, String whitePassword, boolean isEnd, String name) {
        this.blackPassword = blackPassword;
        this.whitePassword = whitePassword;
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

    public String getBlackPassword() {
        return blackPassword;
    }

    public void setBlackPassword(String blackPassword) {
        this.blackPassword = blackPassword;
    }

    public String getWhitePassword() {
        return whitePassword;
    }

    public void setWhitePassword(String whitePassword) {
        this.whitePassword = whitePassword;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }
}
