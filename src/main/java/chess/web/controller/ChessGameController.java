package chess.web.controller;

import chess.web.dto.ScoreDto;
import chess.web.service.ChessGameService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/start")
    public String start() {
        service.start();
        return "redirect:/play";
    }

    @GetMapping("/play")
    public ModelAndView play() {
        ModelAndView modelAndView = new ModelAndView("game");
        return modelAndView;
    }

    @GetMapping("/end")
    public ModelAndView end() {
        ModelAndView modelAndView = new ModelAndView("finished");
        ScoreDto scores = service.status();
        modelAndView.addObject("whiteScore", scores.getWhiteScore());
        modelAndView.addObject("blackScore", scores.getBlackScore());

        return modelAndView;
    }
}
