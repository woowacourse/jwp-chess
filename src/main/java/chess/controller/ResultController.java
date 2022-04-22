package chess.controller;

import chess.dto.GameResultDto;
import chess.service.ChessService;
import chess.util.ResponseUtil;
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
        GameResultDto gameResultDto = chessService.findGameResult(id);
        return ResponseUtil.createModelAndView(HTML_TEMPLATE_PATH, gameResultDto);
    }
}
