package chess.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JoinUserDTO {
    private String playerId;
    private String password;
}
