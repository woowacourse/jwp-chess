package chess.controller;

import chess.dto.SearchResultDto;
import chess.service.ChessService2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final ChessService2 chessService;

    public SearchController(ChessService2 chessService) {
        this.chessService = chessService;
    }

    @PostMapping
    public SearchResultDto searchResult(@RequestParam(name = "game_id") int gameId) {
        return chessService.searchGame(gameId);
    }
}
