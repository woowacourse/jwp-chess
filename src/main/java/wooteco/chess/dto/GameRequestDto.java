package wooteco.chess.dto;

import java.util.UUID;

public class GameRequestDto {

    private UUID id;

    public GameRequestDto(final UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
