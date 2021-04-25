package chess.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class UserDTO {
    private int id;
    private String nickname;
    private String password;

    public UserDTO(int id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
