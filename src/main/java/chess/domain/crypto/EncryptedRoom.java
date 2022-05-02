package chess.domain.crypto;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class EncryptedRoom {

    private String roomName;
    private String encryptedPassword;
}
