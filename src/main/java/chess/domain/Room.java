package chess.domain;

import org.springframework.security.crypto.password.PasswordEncoder;

import chess.dto.RoomRequest;

public class Room {

    private Long id;
    private final String roomName;
    private final String password;

    public Room(Long id, String roomName, String password) {
        this.id = id;
        this.roomName = roomName;
        this.password = password;
    }

    public Room(String roomName, String password) {
        this(null, roomName, password);
    }

    public static Room createRoomEncoded(RoomRequest roomRequest, PasswordEncoder encoder) {
        return new Room(roomRequest.getRoomName(), encoder.encode(roomRequest.getPassword()));
    }

    public Long getId() {
        return id;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getPassword() {
        return password;
    }

    public boolean isPasswordMatches(String inputPassword, PasswordEncoder encoder) {
        return encoder.matches(inputPassword, password);
    }
}
