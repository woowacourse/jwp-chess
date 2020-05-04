package wooteco.chess.dto;

import wooteco.chess.domain.game.NormalStatus;

public class DestinationPositionDto {
    private final String position;
    private final NormalStatus normalStatus;

    public DestinationPositionDto(String position, NormalStatus normalStatus) {
        this.position = position;
        this.normalStatus = normalStatus;
    }

    public String getPosition() {
        return position;
    }

    public NormalStatus getNormalStatus() {
        return normalStatus;
    }
}
