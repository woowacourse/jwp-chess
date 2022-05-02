package chess.dto;

import java.util.List;
import java.util.stream.Collectors;

public class RoomsResponse {

    private List<RoomResponse> rooms;

    private RoomsResponse(List<RoomResponse> rooms) {
        this.rooms = rooms;
    }

    public static RoomsResponse createRoomsResponse(List<RoomResponse> rooms) {
        return new RoomsResponse(rooms.stream()
                .map(room -> new RoomResponse(room.getId(), room.getTitle()))
                .collect(Collectors.toList()));
    }

    public List<RoomResponse> getRooms() {
        return rooms;
    }
}
