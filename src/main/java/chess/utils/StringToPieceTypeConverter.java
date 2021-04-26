package chess.utils;

import chess.domain.piece.PieceType;
import org.springframework.core.convert.converter.Converter;

public class StringToPieceTypeConverter implements Converter<String, PieceType> {

    private static final char EMPTY_LETTER = ' ';

    @Override
    public PieceType convert(final String source) {
        return PieceType.from(
            source.isEmpty() ? EMPTY_LETTER : source.charAt(0)
        );
    }

}
