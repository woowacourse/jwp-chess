package chess.controller2;

import chess.dto.GameCountDto;
import chess.dto.SuccessResponseDto2;
import chess.service.ChessService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/search")
public class SearchController {

    private static final String HTML_TEMPLATE_PATH = "search";
    private static final String RESPONSE = "response";

    private final ChessService chessService = ChessService.getInstance();

    @GetMapping
    public ModelAndView render() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(HTML_TEMPLATE_PATH);
        GameCountDto gameCountDto = chessService.countGames();
        modelAndView.addObject(RESPONSE, gameCountDto);
        return modelAndView;
    }

    @PostMapping
    public SuccessResponseDto2 searchResult(@RequestParam(name="game_id") int gameId) {
        return new SuccessResponseDto2(chessService.searchGame(gameId));
    }
}
