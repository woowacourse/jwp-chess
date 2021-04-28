package chess.web.dto.game;

public class GameRequestDto {

    private final String name;
    private final long hostId;
    private final long guestId;

    public GameRequestDto(final String name, final long hostId, final long guestId) {
        this.name = name;
        this.hostId = hostId;
        this.guestId = guestId;
    }

    public String getName() {
        return name;
    }

    public long getHostId() {
        return hostId;
    }

    public long getGuestId() {
        return guestId;
    }

}
