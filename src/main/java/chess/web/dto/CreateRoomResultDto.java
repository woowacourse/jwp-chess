package chess.web.dto;

public class CreateRoomResultDto {
    private int roomNumber;

    public CreateRoomResultDto() {
    }

    public CreateRoomResultDto(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
}
