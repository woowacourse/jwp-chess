package chess.controller;

import chess.dto.ResponseDto;
import chess.dto.StatusDto;
import chess.service.ChessGameService;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChessSpringController {
    private final ChessGameService chessGameService;

    public ChessSpringController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/start")
    public @ResponseBody
    ResponseDto start() {
        return chessGameService.start();
    }


    @GetMapping("/chess")
    public ModelAndView chess() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("boardDto", chessGameService.getBoard());
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @PostMapping("/move")
    public @ResponseBody
    ResponseDto move(@RequestBody String request) {
        List<String> command = Arrays.asList(request.split(" "));
        return chessGameService.move(command.get(0), command.get(1));
    }

    @GetMapping("/chess-status")
    public ModelAndView status() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView
                .addObject("status", StatusDto.of(chessGameService.statusOfWhite(), chessGameService.statusOfBlack()));
        modelAndView.setViewName("status");
        return modelAndView;
    }

    @GetMapping("/end")
    public @ResponseBody
    ResponseDto end() {
        return chessGameService.end();
    }
}
