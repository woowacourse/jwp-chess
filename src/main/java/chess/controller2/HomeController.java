package chess.controller2;

import chess.dto.GameCountDto;
import chess.dto.SuccessResponseDto2;
import chess.service.ChessService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
public class HomeController {

    private static final String HTML_TEMPLATE_PATH = "home";
    private static final String HOME_DYNAMIC_PROPERTIES = "gameCount";

    private final ChessService chessService = ChessService.getInstance();

    @GetMapping
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(HTML_TEMPLATE_PATH);
        GameCountDto gameCountDto = chessService.countGames();
        modelAndView.addObject(HOME_DYNAMIC_PROPERTIES, gameCountDto);
        return modelAndView;
    }

    @PostMapping
    public SuccessResponseDto2 createGame() {
        return new SuccessResponseDto2(chessService.initGame());
    }
}
