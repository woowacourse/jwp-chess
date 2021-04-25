package chess.webdto.view;

public class ScoreDto {
    private Double white;
    private Double black;

    public ScoreDto(Double white, Double black) {
        this.white = white;
        this.black = black;
    }

    public Double getWhite() {
        return white;
    }

    public Double getBlack() {
        return black;
    }
}
