package chess.dto;

public class GameDto {
    private final boolean running;
    private final boolean whiteTurn;
    private final int no;
    private final String title;
    private final String password;

    public GameDto(boolean running, boolean whiteTurn, int no, String title, String password) {
        this.running = running;
        this.whiteTurn = whiteTurn;
        this.no = no;
        this.title = title;
        this.password = password;
    }

    public GameDto(int no, String title) {
        this(true, true, no, title, "");
    }

    public static GameDto fromNewGame(String title, String password) {
        return new GameDto(true, true, 0, title, password);
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public int getNo() {
        return no;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }
}
