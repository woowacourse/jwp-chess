package chess.dto;

import java.util.List;

public class RoomDto {

    private String gameId;
    private String gamePassword;
    private boolean force_end_flag;

    public RoomDto(String gameId, String gamePassword, boolean force_end_flag) {
        this.gameId = gameId;
        this.gamePassword = gamePassword;
        this.force_end_flag = force_end_flag;
    }

    public String getGameId() {
        return gameId;
    }

    public String getGamePassword() {
        return gamePassword;
    }

    public boolean isForce_end_flag() {
        return force_end_flag;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setGamePassword(String gamePassword) {
        this.gamePassword = gamePassword;
    }

    public void setForce_end_flag(boolean force_end_flag) {
        this.force_end_flag = force_end_flag;
    }
}
