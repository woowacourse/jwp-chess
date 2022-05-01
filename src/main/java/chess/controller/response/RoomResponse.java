package chess.controller.response;

import chess.serviece.dto.GameDto;

public class RoomResponse {

    private final Long id;
    private final String title;

    public RoomResponse(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static RoomResponse from(GameDto gameDto) {
        return new RoomResponse(gameDto.getId(), gameDto.getTitle());
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
