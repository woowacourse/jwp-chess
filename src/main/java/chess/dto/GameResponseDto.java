package chess.dto;

import chess.entity.Game;

public class GameResponseDto {

    private final String name;
    private final String turn;
    private final long hostId;
    private final long guestId;

    private GameResponseDto(String name, String turn, long hostId, long guestId) {
        this.name = name;
        this.turn = turn;
        this.hostId = hostId;
        this.guestId = guestId;
    }

    public static GameResponseDto from(Game game) {
        return new GameResponseDto(game.getName(), game.getTurn(), game.getHostId(), game.getGuestId());
    }

    public String getName() {
        return name;
    }

    public String getTurn() {
        return turn;
    }

    public long getHostId() {
        return hostId;
    }

    public long getGuestId() {
        return guestId;
    }
}
