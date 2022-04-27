package chess.dto;

public class GameDto {
    private final int no;
    private final String title;

    public GameDto(int no, String title) {
        this.no = no;
        this.title = title;
    }

    public int getNo() {
        return no;
    }

    public String getTitle() {
        return title;
    }
}
