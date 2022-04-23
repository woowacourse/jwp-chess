package chess.controller;

import chess.dto.SearchResultDto;
import chess.service.ChessService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final ChessService chessService;

    public SearchController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping
    public SearchResultDto searchResult(@RequestParam(name = "game_id") int gameId) {
        return chessService.searchGame(gameId);
    }
}
