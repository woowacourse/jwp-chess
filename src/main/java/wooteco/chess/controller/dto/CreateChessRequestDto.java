package wooteco.chess.controller.dto;

public class CreateChessRequestDto {
    private String title;

    public CreateChessRequestDto() {
    }

    public CreateChessRequestDto(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
