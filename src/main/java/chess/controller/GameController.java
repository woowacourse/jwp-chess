package chess.controller;

import chess.service.GameService;
import chess.service.RoomService;
import chess.view.OutputView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/game")
public class GameController {
    private final RoomService roomService;
    private final GameService gameService;

    public GameController(final RoomService roomService, final GameService gameService) {
        this.roomService = roomService;
        this.gameService = gameService;
    }

    @GetMapping("/load/{roomId}")
    public String loadGame(@PathVariable final Long roomId, final Model model) {
        if (gameService.isGameEnd(roomId)) {
            return "redirect:/game/result/" + roomId;
        }

        model.addAttribute("room", roomService.roomInfo(roomId));
        model.addAttribute("game", gameService.gameInfo(roomId));
        return "chessBoardPage";
    }

    @GetMapping("/result/{roomId}")
    public String printResult(@PathVariable final Long roomId, final Model model) {
        final String winnerName = OutputView.decideWinnerName(gameService.winner(roomId));
        model.addAttribute("winner", winnerName);
        return "winningResultPage";
    }
}