package chess.dto;

public class RoomExistResponseDto {

    private int isExist;

    public RoomExistResponseDto(int isExist) {
        this.isExist = isExist;
    }

    public int getIsExist() {
        return isExist;
    }
}
