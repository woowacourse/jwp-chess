package chess.application.web;

import chess.dto.BoardResponse;
import chess.dto.GameResponse;
import chess.dto.ResultResponse;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewController {
    private final GameService gameService;

    public ViewController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public ModelAndView games() {
        List<GameResponse> games = gameService.findAllGames();
        return new ModelAndView("games", "games", games);
    }

    @GetMapping("/create-view")
    public String createView() {
        return "create";
    }

    @GetMapping("/delete-view/{id}")
    public String delete(@PathVariable String id) {
        return "delete";
    }

    @GetMapping("/games/{id}")
    public ModelAndView findGameById(@PathVariable Long id) {
        BoardResponse boardResponse = gameService.findBoardByGameId(id);
        return new ModelAndView("game", "boardResponse", boardResponse);
    }

    @GetMapping("/games/{id}/end")
    public ModelAndView end(@PathVariable Long id) {
        ResultResponse resultResponse = gameService.end(id);
        return new ModelAndView("result", "result", resultResponse);
    }
}
