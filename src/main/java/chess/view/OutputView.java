package chess.view;

import chess.domain.piece.Owner;

import java.util.List;

public class OutputView {

    private static int SIZE_OF_ONLY_WINNER = 1;

    private OutputView() {

    }

    public static String decideWinnerName(final List<Owner> winners) {
        if (winners.size() == SIZE_OF_ONLY_WINNER) {
            final Owner winner = winners.get(0);
            return winner.name();
        }
        return "무승부";
    }
}
