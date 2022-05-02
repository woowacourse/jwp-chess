package chess.dto.request.web;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class CreateRoomRequest {
    private String roomName;
    private String roomPassword;
}
