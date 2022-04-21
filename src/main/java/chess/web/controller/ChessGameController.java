package chess.web.controller;

import chess.web.dto.MoveDto;
import chess.web.dto.MoveResultDto;
import chess.web.service.ChessGameService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChessGameController {

    private final ChessGameService service;

    public ChessGameController(ChessGameService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ModelAndView root() {
        ModelAndView modelAndView = new ModelAndView("start");
        return modelAndView;
    }

    @GetMapping("/start")
    public String start() {
        service.start();
        return "redirect:/play";
    }

    @GetMapping("/play")
    public ModelAndView play() {
        return service.play();
    }

    @PostMapping("/move")
    @ResponseBody
    public MoveResultDto move(@RequestBody MoveDto moveDto) {
        return service.move(moveDto);
    }

    @GetMapping("/end")
    public ModelAndView end() {
        ModelAndView modelAndView = new ModelAndView("end");
        return modelAndView;
    }
}
