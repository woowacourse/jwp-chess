package chess.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class UserDTO {
    private final int id;
    private final String nickname;
}
