package chess.entity;

import java.time.LocalDateTime;

public class Game {

    private final long id;
    private final String name;
    private final long hostId;
    private final long guestId;
    private final String turn;
    private final boolean isFinished;
    private final LocalDateTime createdTime;

    public Game(long id, String name, long hostId, long guestId, String turn, boolean isFinished,
        LocalDateTime createdTime) {
        this.id = id;
        this.name = name;
        this.hostId = hostId;
        this.guestId = guestId;
        this.turn = turn;
        this.isFinished = isFinished;
        this.createdTime = createdTime;
    }

    public long getId() {
        return id;
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

    public String getTurn() {
        return turn;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }
}

