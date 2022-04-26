package chess.controller;

import chess.controller.view.BoardView;
import chess.dto.LogInDto;
import chess.service.ChessGameService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChessController {
    private final ChessGameService chessGameService;

    public ChessController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/start")
    public String startGame(@RequestParam String gameId, @RequestParam String gamePassword) {
        chessGameService.createOrGet(gameId);
        return "redirect:/game/" + gameId;
    }

    @GetMapping(path = "/game/{gameId}")
    public ModelAndView getGame(@PathVariable String gameId) {
        return getModel(gameId);
    }

    private ModelAndView getModel(String gameId) {
        ModelAndView modelAndView = new ModelAndView("game");
        modelAndView.addObject("pieces", BoardView.of(chessGameService.getCurrentGame(gameId)).getBoardView());
        modelAndView.addObject("gameId", gameId);
        modelAndView.addObject("status", chessGameService.calculateGameResult(gameId));
        return modelAndView;
    }
}
