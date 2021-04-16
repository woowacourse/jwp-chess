package chess.controller;

import chess.dto.BoardDto;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MenuController {

    private final ChessService service;

    public MenuController(ChessService service) {
        this.service = service;
    }

    @GetMapping
    public String renderInitBoard() {
        return "index.html";
    }

    @ResponseBody
    @GetMapping("/load")
    public ResponseEntity<BoardDto> loadBoard() {
        return ResponseEntity.ok(new BoardDto(service.findBoard()));
    }
}
