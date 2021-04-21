package chess.dto;

public class ChessGamesSaveDto {

    private String title;

    public ChessGamesSaveDto() {
    }

    public ChessGamesSaveDto(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
