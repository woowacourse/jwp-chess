package chess.controller;

import chess.service.GameService;
import chess.service.RoomService;
import chess.view.OutputView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView loadGame(@PathVariable final Long roomId) {
        if (gameService.isGameEnd(roomId)) {
            return new ModelAndView("redirect:/game/result/" + roomId);
        }

        final ModelAndView view = new ModelAndView("chessBoardPage");
        view.addObject("room", roomService.roomInfo(roomId));
        view.addObject("game", gameService.gameInfo(roomId));
        return view;
    }

    @GetMapping("/result/{roomId}")
    public ModelAndView printResult(@PathVariable final Long roomId) {
        final String winnerName = OutputView.decideWinnerName(gameService.winner(roomId));

        final ModelAndView view = new ModelAndView("winningResultPage");
        view.addObject("winner", winnerName);
        return view;
    }
}