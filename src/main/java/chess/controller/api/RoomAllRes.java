package chess.controller.api;

import chess.entity.Room;
import java.util.List;
import java.util.stream.Collectors;

public class RoomAllRes {

    private List<RoomRes> rooms;

    private RoomAllRes(List<RoomRes> rooms) {
        this.rooms = rooms;
    }

    public static RoomAllRes createRoomAllRes(List<Room> rooms) {
        return new RoomAllRes(rooms.stream()
                .map(room -> new RoomRes(room.getId(), room.getTitle()))
                .collect(Collectors.toList()));
    }

    public List<RoomRes> getRooms() {
        return rooms;
    }
}
