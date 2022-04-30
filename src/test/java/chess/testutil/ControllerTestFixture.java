package chess.testutil;

import chess.entity.RoomEntity;
import chess.util.PasswordSha256Encoder;

public class ControllerTestFixture {
    public static final String REQUEST_MAPPING_URI = "/api/chess/rooms";
    public static final RoomEntity ROOM_A = new RoomEntity(1L, PasswordSha256Encoder.encode("1234"), "체스 초보만", "white",
        false);
    public static final RoomEntity ROOM_B = new RoomEntity(2L, PasswordSha256Encoder.encode("1234"), "체스 초보만", "white",
        false);
}
