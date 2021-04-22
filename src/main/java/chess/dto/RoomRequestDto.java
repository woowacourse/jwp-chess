package chess.dto;

import java.beans.ConstructorProperties;
import lombok.Getter;

@Getter
public class RoomRequestDto {
    private final String name;

    @ConstructorProperties({"name"})
    RoomRequestDto (final String name) {
        this.name = name;
    }
}
