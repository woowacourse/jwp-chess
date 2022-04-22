package chess.controller;

import chess.dto.GameCountDto;
import chess.service.ChessService;
import chess.util.ResponseUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {

    private static final String HTML_TEMPLATE_PATH = "home";

    private final ChessService chessService;

    public HomeController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping
    public ModelAndView home() {
        GameCountDto gameCountDto = chessService.countGames();
        return ResponseUtil.createModelAndView(HTML_TEMPLATE_PATH, gameCountDto);
    }
}
