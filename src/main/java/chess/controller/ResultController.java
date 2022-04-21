package chess.controller;

import chess.dto.GameResultDto;
import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/result")
public class ResultController {

    private static final String HTML_TEMPLATE_PATH = "result";

    private final ChessService chessService;

    public ResultController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/{id}")
    public ModelAndView result(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(HTML_TEMPLATE_PATH);
        GameResultDto gameResultDto = chessService.findGameResult(id);
        modelAndView.addObject("response", gameResultDto);
        return modelAndView;
    }
}
