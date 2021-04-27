package chess.dto.player;

import chess.domain.entity.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class PlayerDTO {
    private int id;
    private String nickname;
    private String password;

    public PlayerDTO(int id, String nickname) {
        this(id, nickname, null);
    }

    public PlayerDTO(Player player) {
        this(player.getId(), player.getNickname(), player.getPassword());
    }
}
