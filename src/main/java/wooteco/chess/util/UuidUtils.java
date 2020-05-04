package wooteco.chess.util;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UuidUtils {
    public static UUID byteToUuid(byte[] source) {
        ByteBuffer bb = ByteBuffer.wrap(source);
        long firstLong = bb.getLong();
        long secondLong = bb.getLong();
        return new UUID(firstLong, secondLong);
    }

    public static byte[] uuidToByte(UUID source) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(source.getMostSignificantBits());
        bb.putLong(source.getLeastSignificantBits());
        return bb.array();
    }
}
