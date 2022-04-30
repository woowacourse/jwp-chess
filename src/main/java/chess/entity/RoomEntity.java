package chess.entity;

public class RoomEntity {
    private Long id;
    private String password;
    private String name;
    private String team;
    private Boolean gameOver;

    public RoomEntity(final Long id, final String password, final String name,
                      final String team, final Boolean gameOver) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.team = team;
        this.gameOver = gameOver;
    }

    public RoomEntity(final String password, final String name, final String team,
                      final Boolean gameOver) {
        this.id = null;
        this.password = password;
        this.name = name;
        this.team = team;
        this.gameOver = gameOver;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
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
        if (roomEntity.password != null) {
            this.password = roomEntity.password;
        }
        if (roomEntity.team != null) {
            this.team = roomEntity.team;
        }
        if (roomEntity.gameOver != null) {
            this.gameOver = roomEntity.gameOver;
        }
    }
}
