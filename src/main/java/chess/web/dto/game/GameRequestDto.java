package chess.web.dto.game;

public class GameRequestDto {

    private final String name;
    private final long hostId;

    public GameRequestDto(final String name, final long hostId) {
        this.name = name;
        this.hostId = hostId;
    }

    public String getName() {
        return name;
    }

    public long getHostId() {
        return hostId;
    }

}
