package chess.controller;

import chess.Dto.BoardDto;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MenuController {

    private final ChessService service;

    public MenuController(ChessService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String renderInitBoard() {
        return "index.html";
    }

    @ResponseBody
    @GetMapping("/load")
    public BoardDto loadBoard() {
        return new BoardDto(service.findBoard());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<RuntimeException> handle(RuntimeException e) {
        return ResponseEntity.badRequest().body(new RuntimeException(e.getMessage()));
    }

}
