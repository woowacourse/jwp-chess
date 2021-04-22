package chess.view;

import chess.domain.piece.Owner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

public class OutputView {

    private static final int SIZE_OF_ONLY_WINNER = 1;

    private OutputView() {
    }

    public static String decideWinnerName(final List<Owner> winners) {
        if (winners.size() == SIZE_OF_ONLY_WINNER) {
            final Owner winner = winners.get(0);
            return winner.name();
        }
        return "무승부";
    }

    public static String getErrorMessage(final List<FieldError> fieldError){
        return fieldError.stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }
}
