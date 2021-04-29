package chess.dto.room;

import chess.domain.GameStatus;
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
        this.status = GameStatus.status(status);
        this.playing = playing;
    }
}
