package chess.controller.dto.response;

public class ColorResponse {

    private final String color;

    public ColorResponse(final String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
