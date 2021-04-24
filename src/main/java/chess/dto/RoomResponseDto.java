package chess.dto;

import java.util.List;

public class RoomResponseDto {

    private final String roomNumbers;

    private RoomResponseDto(String roomNumbers) {
        this.roomNumbers = roomNumbers;
    }

    public static RoomResponseDto from(List<String> roomNumbers) {
        StringBuffer sb = new StringBuffer();
        for (String roomNumber : roomNumbers) {
            sb.append(roomNumber + " ");
        }
        return new RoomResponseDto(sb.toString());
    }

    public String getRoomNumbers() {
        return roomNumbers;
    }

}
