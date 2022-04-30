package chess.dto.request;

import chess.entity.RoomEntity;
import chess.util.PasswordSha256Encoder;

public class RoomRequestDto {

    private String name;
    private String password;

    public RoomRequestDto() {
    }

    public RoomRequestDto(final String password) {
        this.password = password;
    }

    public RoomRequestDto(final String name, final String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public RoomEntity toEntity() {
        return new RoomEntity(null, PasswordSha256Encoder.encode(this.password), name, null, null);
    }
}
