package chess.dto.response;

import chess.entity.RoomEntity;

public class RoomResponseDto {

    private Long id;
    private String name;
    private String team;
    private boolean gameOver;

    private RoomResponseDto(final Long id, final String name, final String team, final boolean gameOver) {
        this.id = id;
        this.name = name;
        this.team = team;
        this.gameOver = gameOver;
    }

    public static RoomResponseDto of(final RoomEntity room) {
        return new RoomResponseDto(room.getId(), room.getName(), room.getTeam(), room.isGameOver());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTeam() {
        return team;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
