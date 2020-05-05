package wooteco.chess.repository;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import wooteco.chess.util.UuidUtils;

import java.util.UUID;

@WritingConverter
public class UUIDtoBinaryConverter implements Converter<UUID, byte[]> {
    @Override
    public byte[] convert(UUID source) {
        return UuidUtils.uuidToByte(source);
    }
}
