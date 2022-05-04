package chess.service.dto.response;

public class PositionResponseDto {

    private final String position;

    public PositionResponseDto(final String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }
}
