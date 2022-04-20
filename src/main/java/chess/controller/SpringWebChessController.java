package chess.controller;

import chess.domain.dto.ResponseDto;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SpringWebChessController {

    private final ChessService chessService;

    public SpringWebChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index() {
        return "game";
    }

    @PostMapping("/start")
    private ResponseEntity<ResponseDto> start() {
        return ResponseEntity
                .ok()
                .body(chessService.start());
    }
}
