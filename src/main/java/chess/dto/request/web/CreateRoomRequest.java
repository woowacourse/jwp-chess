package chess.dto.request.web;


import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class CreateRoomRequest {
    private String roomName;
    private String encryptedRoomPassword;
    private Map<String, String> pieces;
}
