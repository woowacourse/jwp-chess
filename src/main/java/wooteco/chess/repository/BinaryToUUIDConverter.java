package wooteco.chess.repository;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import wooteco.chess.util.UuidUtils;

import java.nio.ByteBuffer;
import java.util.UUID;

@ReadingConverter
public class BinaryToUUIDConverter implements Converter<byte[], UUID> {
    @Override
    public UUID convert(byte[] source) {
        return UuidUtils.byteToUuid(source);
    }
}
