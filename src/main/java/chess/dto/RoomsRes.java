package chess.dto;

import java.util.List;
import java.util.stream.Collectors;

public class RoomsRes {

    private List<RoomRes> rooms;

    private RoomsRes(List<RoomRes> rooms) {
        this.rooms = rooms;
    }

    public static RoomsRes createRoomAllRes(List<RoomRes> rooms) {
        return new RoomsRes(rooms.stream()
                .map(room -> new RoomRes(room.getId(), room.getTitle()))
                .collect(Collectors.toList()));
    }

    public List<RoomRes> getRooms() {
        return rooms;
    }
}
