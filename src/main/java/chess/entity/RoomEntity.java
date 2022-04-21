package chess.entity;

public class RoomEntity {
    private Long id;
    private String name;
    private String team;
    private boolean gameOver;

    public RoomEntity(final Long id, final String name, final String team, final boolean gameOver) {
        this.id = id;
        this.name = name;
        this.team = team;
        this.gameOver = gameOver;
    }

    public RoomEntity(final String name, final String team, final boolean gameOver) {
        this.name = name;
        this.team = team;
        this.gameOver = gameOver;
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
