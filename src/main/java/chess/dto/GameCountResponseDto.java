package chess.dto;

public class GameCountResponseDto {

    private final int count;

    public GameCountResponseDto(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
