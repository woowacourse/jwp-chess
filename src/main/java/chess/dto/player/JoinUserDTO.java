package chess.dto.player;

import chess.dto.room.RoomCreateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JoinUserDTO {
    private String nickname;
    private String password;

    public JoinUserDTO(RoomCreateDTO roomCreateDTO) {
        this(roomCreateDTO.getNickname(), roomCreateDTO.getPassword());
    }
}
