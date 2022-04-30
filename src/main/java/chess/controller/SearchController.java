package chess.controller;

import chess.dto.GameCountDto;
import chess.dto.GameInfoDto;
import chess.dto.GamesResponse;
import chess.service.ChessService;
import chess.util.ResponseUtil;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/search")
public class SearchController {

    private static final String HTML_TEMPLATE_PATH = "search";

    private final ChessService chessService;

    public SearchController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping
    public ModelAndView renderSearchPage() {
        GameCountDto gameCountDto = chessService.countGames();
        List<GameInfoDto> gameInfoDtos = chessService.selectAllGames();
        GamesResponse gamesResponse = new GamesResponse(gameCountDto, gameInfoDtos);

        return ResponseUtil.createModelAndView(HTML_TEMPLATE_PATH, gamesResponse);
    }
}
