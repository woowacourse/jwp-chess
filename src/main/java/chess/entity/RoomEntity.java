package chess.entity;

public class RoomEntity {
    private Long id;
    private String name;
    private String team;
    private Boolean gameOver;

    public RoomEntity(final Long id, final String name, final String team, final Boolean gameOver) {
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

    public void patch(final RoomEntity roomEntity) {
        if (roomEntity.name != null) {
            this.name = roomEntity.name;
        }
        if (roomEntity.team != null) {
            this.team = roomEntity.team;
        }
        if (roomEntity.gameOver != null) {
            this.gameOver = roomEntity.gameOver;
        }
    }
}
