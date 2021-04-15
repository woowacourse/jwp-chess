package chess.view;

import chess.controller.dto.GameInfoDto;
import chess.controller.dto.RoomInfoDto;
import chess.domain.piece.Owner;
import org.springframework.ui.Model;

import java.util.List;

public class OutputView {

    private static int SIZE_OF_ONLY_WINNER = 1;

    private OutputView() {
    }

    public static String printWinningResult(Model model, List<Owner> winner) {
        model.addAttribute("winner", OutputView.decideWinnerName(winner));
        return "result";
    }

    public static String printGame(Model model, RoomInfoDto roomInfo, GameInfoDto gameInfo) {
        model.addAttribute("room", roomInfo);
        model.addAttribute("game", gameInfo);
        return "chessBoard";
    }

    public static String printMainPage(Model model, List<RoomInfoDto> loadList) {
        model.addAttribute("list", loadList);
        return "mainPage";
    }

    public static String printErrorPage(Model model, String message) {
        model.addAttribute("msg", message);
        return "errorPage";
    }

    private static String decideWinnerName(final List<Owner> winners) {
        if (winners.size() == SIZE_OF_ONLY_WINNER) {
            final Owner winner = winners.get(0);
            return winner.name();
        }
        return "무승부";
    }
}
