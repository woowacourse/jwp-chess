package chess.controller;

import chess.service.ChessService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SpringController {

    private final ChessService chessService;

    @Autowired
    public SpringController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/start")
    public ModelAndView start(@RequestParam("game_name") String gameName) {
        List<String> chessBoard = chessService.findByName(gameName);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("chessboard", chessBoard);
        modelAndView.setViewName("chess");

        return modelAndView;
    }


}
