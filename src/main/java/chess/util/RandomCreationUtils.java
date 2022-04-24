package chess.util;

import java.util.UUID;

public class RandomCreationUtils {

    public static String createUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
