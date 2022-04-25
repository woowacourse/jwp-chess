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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final RoomEntity that = (RoomEntity) o;

        if (isGameOver() != that.isGameOver()) {
            return false;
        }
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
            return false;
        }
        return getTeam() != null ? getTeam().equals(that.getTeam()) : that.getTeam() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getTeam() != null ? getTeam().hashCode() : 0);
        result = 31 * result + (isGameOver() ? 1 : 0);
        return result;
    }
}
