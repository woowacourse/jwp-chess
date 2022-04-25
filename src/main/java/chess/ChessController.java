package chess;

import chess.domain.Status;
import chess.dto.BoardDto;
import chess.dto.ExceptionResponseDto;
import chess.dto.MoveDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index() {
        return "roby.html";
    }

    @GetMapping("/room")
    public String room(@RequestParam String name,
                       Model model) {
        chessService.createRoom(name);
        model.addAttribute("name", name);
        return "room.html";
    }

    @GetMapping("/start")
    @ResponseBody
    public BoardDto start(@RequestParam String name) {
        return chessService.startNewGame(name);
    }

    @GetMapping("/load")
    @ResponseBody
    public BoardDto load(@RequestParam String name) {
        return chessService.load(name);
    }

    @PostMapping("/move")
    @ResponseBody
    public BoardDto move(@RequestParam String name,
                         @RequestBody MoveDto moveDto) {
        return chessService.move(name, moveDto);
    }

    @GetMapping("/status")
    @ResponseBody
    public Status status(@RequestParam String name) {
        return chessService.status(name);
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<ExceptionResponseDto> handle(RuntimeException exception) {
        return ResponseEntity.badRequest()
                .body(new ExceptionResponseDto(exception.getMessage()));
    }
}
