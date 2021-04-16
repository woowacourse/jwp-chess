package chess.controller;

import chess.service.ChessService;
import chess.service.dto.TilesDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChessGameController {

    private final ChessService chessService;

    public ChessGameController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String main(){
        return "main";
    }

    @GetMapping("/games")
    public String startPage(Model model){
        TilesDto tilesDto = chessService.emptyBoard();
        model.addAttribute("tilesDto", tilesDto);
        return "board";
    }
}
