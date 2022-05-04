package chess.service.dto.response;

public class ColorResponseDto {

    private final String color;

    public ColorResponseDto(final String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
