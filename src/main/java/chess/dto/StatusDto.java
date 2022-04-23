package chess.dto;

import java.util.Objects;

public class StatusDto {
    private final String white;
    private final String black;

    private StatusDto(String statusOfWhite, String statusOfBlack) {
        this.white = statusOfWhite;
        this.black = statusOfBlack;
    }

    public static StatusDto of(double statusOfWhite, double statusOfBlack) {
        return new StatusDto(String.format("%.1f", statusOfWhite), String.format("%.1f", statusOfBlack));
    }

    public String getWhite() {
        return white;
    }

    public String getBlack() {
        return black;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StatusDto statusDto = (StatusDto) o;
        return Objects.equals(white, statusDto.white) && Objects.equals(black, statusDto.black);
    }

    @Override
    public int hashCode() {
        return Objects.hash(white, black);
    }
}
