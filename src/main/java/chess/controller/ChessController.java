package chess.controller;

import chess.controller.view.BoardView;
import chess.dto.GamePlayDto;
import chess.service.ChessGameService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChessController {
    private final ChessGameService chessGameService;

    public ChessController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/game/start")
    public String startGame(@RequestParam String gameId) {
        chessGameService.createOrGet(gameId);
        return "redirect:/game/" + gameId;
    }

    @GetMapping(path = "/game/{gameId}")
    public ModelAndView getGame(@PathVariable String gameId,
                                @RequestParam(value = "message", required = false, defaultValue = "") String message) {
        return getModel(new GamePlayDto(gameId, message));
    }

    private ModelAndView getModel(GamePlayDto gamePlayDto) {
        ModelAndView modelAndView = new ModelAndView("game");
        String gameId = gamePlayDto.getGameId();
        modelAndView.addObject("pieces", BoardView.of(chessGameService.getCurrentGame(gameId)).getBoardView());
        modelAndView.addObject("gameId", gameId);
        modelAndView.addObject("status", chessGameService.calculateGameResult(gameId));
        modelAndView.addObject("gameMessage", gamePlayDto.getGameMessage());
        return modelAndView;
    }
}
