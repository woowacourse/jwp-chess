package wooteco.chess.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import wooteco.chess.domain.position.Position;

@WritingConverter
public class PositionToStringConverter implements Converter<Position, String> {
	@Override
	public String convert(Position source) {
		return source.getName();
	}
}
