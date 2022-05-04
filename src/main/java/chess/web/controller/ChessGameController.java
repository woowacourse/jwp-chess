package chess.web.controller;

import chess.web.service.ChessGameService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChessGameController {

    private final ChessGameService service;

    public ChessGameController(ChessGameService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index", HttpStatus.OK);
    }

    @GetMapping("/chess-game/{id}")
    public ModelAndView play(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView("game");
        return modelAndView;
    }

    @GetMapping("/chess-game/{id}/end")
    public ModelAndView end(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView("finished");
        return modelAndView;
    }
}
