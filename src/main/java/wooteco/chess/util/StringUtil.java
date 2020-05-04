package wooteco.chess.util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class StringUtil {

    private static final String DELIMITER = " ";

    public static List<String> splitChessCommand(final String chessCommand) {
        Objects.requireNonNull(chessCommand, "분리할 명령어가 null입니다.");

        return Arrays.stream(chessCommand.split(DELIMITER))
                .map(String::trim)
                .collect(toList());
    }

}
