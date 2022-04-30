package chess.testutil;

import chess.entity.RoomEntity;

public class ControllerTestFixture {
    public static final String REQUEST_MAPPING_URI = "/api/chess/rooms";
    public static final RoomEntity ROOM_A = new RoomEntity(1L, "1234", "체스 초보만", "white", false);
    public static final RoomEntity ROOM_B = new RoomEntity(2L, "1234", "체스 초보만", "white", false);
}
