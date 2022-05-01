package chess.entity;

public class GameEntity {

    private final int id;
    private final String title;
    private final String password;
    private final boolean running;

    public GameEntity(int id, String title, String password, boolean running) {
        this.id = id;
        this.title = title;
        this.password = password;
        this.running = running;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public String toString() {
        return "GameInfoDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", password='" + password + '\'' +
                ", running=" + running +
                '}';
    }
}