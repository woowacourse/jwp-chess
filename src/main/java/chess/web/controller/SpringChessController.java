package chess.web.controller;

import chess.service.ChessService;
import chess.service.dto.BoardDto;
import chess.service.dto.GameResultDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SpringChessController {

    private final ChessService chessService;

    public SpringChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String renderIndex(Model model) {
        model.addAttribute("games", chessService.getAllGames());
        return "index";
    }

    @GetMapping("/game/{gameId}")
    public String renderBoard(@PathVariable int gameId, Model model) {
        if (chessService.isEnd(gameId)) {
            return "redirect:../status/" + gameId;
        }
        BoardDto board = chessService.getBoard(gameId);
        model.addAttribute("board", board);
        return "board";
    }

    @GetMapping("/board/{gameId}")
    @ResponseBody
    public ResponseEntity<BoardDto> getBoardPieces(@PathVariable int gameId) {
        return ResponseEntity.ok(chessService.getBoard(gameId));
    }

    @PutMapping("/new-board/{gameId}")
    public ResponseEntity<Object> initBoard(@PathVariable int gameId) {
        chessService.initGame(gameId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/board")
    public String createGame(@RequestParam String name, @RequestParam String password) {
        chessService.createGame(name.trim(), password);
        return "redirect:/";
    }

    @PostMapping("/move/{gameId}")
    public String requestMove(@PathVariable int gameId, @RequestParam String from,
                              @RequestParam String to) {
        chessService.move(gameId, from, to);
        return "redirect:../game/" + gameId;
    }

    @GetMapping("/status/{gameId}")
    public String renderStatus(@PathVariable int gameId, Model model) {
        GameResultDto status = chessService.getResult(gameId);
        chessService.endGame(gameId);
        model.addAttribute("status", status);
        return "result";
    }

    @GetMapping("/game-end/{gameId}")
    public String requestEndGame(@PathVariable int gameId) {
        chessService.endGame(gameId);
        return "redirect:../";
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<String> handleError(Exception ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
