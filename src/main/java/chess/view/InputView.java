package chess.view;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class InputView {

    private static final String DELIMITER = " ";

    private static final Scanner scanner = new Scanner(System.in);

    public static Command inputInitialCommand() {
        String input = scanner.nextLine().toLowerCase(Locale.ROOT);
        return Command.of(input);
    }

    public static List<String> inputProgressCommand() {
        return Arrays.stream(scanner.nextLine().split(DELIMITER))
            .map(value -> value.toLowerCase(Locale.ROOT))
            .collect(toList());
    }
}
