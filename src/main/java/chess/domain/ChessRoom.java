package chess.domain;

import chess.dto.RoomDto;

public class ChessRoom {

    private static final int ROOM_NAME_MAX_LENGTH = 25;
    private static final int ROOM_PASSWORD_MAX_LENGTH = 15;

    private final String name;
    private final String password;

    private ChessRoom(String name, String password) {
        this.name = name;
        this.password = password;
        validateChessRoom();
    }

    public static ChessRoom from(final RoomDto roomDto) {
        return new ChessRoom(roomDto.getName(), roomDto.getPassword());
    }

    private void validateChessRoom() {
        if (name.isBlank() || name.length() > ROOM_NAME_MAX_LENGTH) {
            throw new IllegalArgumentException("체스방 제목은 1자 이상 25자 이하여야 합니다.");
        }
        if (password.isBlank() || password.length() > ROOM_PASSWORD_MAX_LENGTH) {
            throw new IllegalArgumentException("체스방 비밀번호는 1자 이상 15자 이하여야 합니다.");
        }
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
