package chess.dto.room;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public final class RoomNameDTO {
    @NotBlank(message = "방 이름은 한글자 이상이어야합니다.")
    private String name;
}
