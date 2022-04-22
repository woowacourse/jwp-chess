package chess;

import chess.domain.Status;
import chess.dto.BoardDto;
import chess.dto.ExceptionResponseDto;
import chess.dto.MoveDto;
import chess.service.ChessService;
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
public class ChessSpringController {

    private final ChessService chessService;

    public ChessSpringController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index() {
        return "roby";
    }

    @GetMapping("/room")
    public String room(@RequestParam(value = "name") String name,
                       Model model) {
        chessService.createRoom(name);
        model.addAttribute("name", name);
        return "room";
    }

    @GetMapping("/start")
    @ResponseBody
    public BoardDto start(@RequestParam(value = "name") String name) {
        return chessService.startNewGame(name);
    }

    @GetMapping("/load")
    @ResponseBody
    public BoardDto load(@RequestParam(value = "name") String name) {
        return chessService.load(name);
    }

    @PostMapping("/move")
    @ResponseBody
    public BoardDto move(@RequestParam(value = "name") String name,
                         @RequestBody MoveDto moveDto) {
        return chessService.move(name, moveDto);
    }

    @GetMapping("/status")
    @ResponseBody
    public Status status(@RequestParam(value = "name") String name) {
        return chessService.status(name);
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<ExceptionResponseDto> handle(RuntimeException exception) {
        return ResponseEntity.badRequest()
                .body(new ExceptionResponseDto(exception.getMessage()));
    }
}
