package chess.dto.room;

import lombok.Getter;

@Getter
public final class RoomDTO {
    private final int id;
    private final String title;
    private final String blackUser;
    private final String whiteUser;
    private final String status;
    private final boolean playing;

    public RoomDTO(final int id, final String title, final String blackUser, final String whiteUser,
                   final int status, final boolean playing) {
        this.id = id;
        this.title = title;
        this.blackUser = blackUser;
        this.whiteUser = whiteUser;
        this.status = status(status);
        this.playing = playing;
    }

    private String status(final int status) {
        if (status == 1) {
            return "진행중";
        }
        if (status == 2) {
            return "준비중";
        }
        return "종료됨";
    }
}
