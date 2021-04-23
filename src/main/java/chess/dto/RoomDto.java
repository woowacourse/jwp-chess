package chess.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RoomDto {

    private final int id;
    private final String name;
}
