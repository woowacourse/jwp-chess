package chess.web;

import chess.service.ChessService;
import org.springframework.stereotype.Controller;

@Controller
public class MenuController {

    private final ChessService service;

    public MenuController(ChessService service) {
        this.service = service;
    }

/*    @GetMapping
    public String renderInitBoard() {
        return "game.mustache";
    }

    @ResponseBody
    @GetMapping("/load")
    public ResponseEntity<BoardDto> loadBoard() {
        return ResponseEntity.ok(new BoardDto(service.findBoard()));
    }*/
}
