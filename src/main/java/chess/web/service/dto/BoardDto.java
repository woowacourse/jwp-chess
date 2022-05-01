package chess.web.service.dto;

public class BoardDto {

    private final Long id;
    private final String turn;
    private final String title;

    public BoardDto(Long id, String turn, String title) {
        this.id = id;
        this.turn = turn;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTurn() {
        return turn;
    }

    public String getTitle() {
        return title;
    }
}
