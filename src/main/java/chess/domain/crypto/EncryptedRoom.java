package chess.domain.crypto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class EncryptedRoom {

    private String name;
    private String encryptedPassword;
}
