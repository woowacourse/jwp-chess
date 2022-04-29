package chess.service.dto.response;

import java.util.Map;

public class PlayersResponseDto {

    private final Map<ColorResponseDto, PlayerResponseDto> playerResponseDtos;

    public PlayersResponseDto(final Map<ColorResponseDto, PlayerResponseDto> playerResponseDtos) {
        this.playerResponseDtos = playerResponseDtos;
    }

    public Map<ColorResponseDto, PlayerResponseDto> getPlayerResponseDtos() {
        return playerResponseDtos;
    }
}
