package chess.framework.formatter;

import chess.domain.board.position.Position;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

public class StringToPositionFormatter implements Formatter<Position> {

    @Override
    public Position parse(String text, Locale locale) throws ParseException {
        return Position.of(text);
    }

    @Override
    public String print(Position object, Locale locale) {
        return "not implementation";
    }
}
