package chess.controller;

import chess.dto.ResponseDto;
import chess.service.ChessGameService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
    public @ResponseBody ResponseDto start(){
        return chessGameService.start();
    }


    @GetMapping("/chess")
    public ModelAndView chess(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("boardDto", chessGameService.getBoard());
        modelAndView.setViewName("index");
        return modelAndView;
    }
}
