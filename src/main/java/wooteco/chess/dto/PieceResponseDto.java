package wooteco.chess.dto;

public class PieceResponseDto {
    private final String position;
    private final String imgUrl;

    public PieceResponseDto(String position, String imgUrl) {
        this.position = position;
        this.imgUrl = imgUrl;
    }

    public String getPosition() {
        return position;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
