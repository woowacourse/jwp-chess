package chess.controller.spring.vo;

import java.io.Serializable;
import java.util.Objects;

public class SessionVO implements Serializable {

    private final int roomId;
    private final String password;

    public SessionVO(int roomId, String password) {
        this.roomId = roomId;
        this.password = password;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionVO sessionVO = (SessionVO) o;
        return roomId == sessionVO.roomId && password.equals(sessionVO.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, password);
    }
}
