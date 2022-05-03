package chess.dto.response;

import chess.entity.RoomEntity;

public class RoomResponseDto {

    private Long id;
    private String name;
    private String status;
    private String turn;

    public RoomResponseDto() {
    }

    public RoomResponseDto(final Long id, final String name, final String status, final String turn) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.turn = turn;
    }

    public static RoomResponseDto from(final RoomEntity room) {
        return new RoomResponseDto(room.getId(), room.getName(), room.getStatus(), room.getTurn());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getTurn() {
        return turn;
    }
}
