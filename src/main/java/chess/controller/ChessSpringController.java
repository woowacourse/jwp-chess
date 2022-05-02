package chess.controller;

import chess.domain.board.strategy.WebBasicBoardStrategy;
import chess.dto.ErrorDto;
import chess.dto.GameDto;
import chess.dto.GameStatusDto;
import chess.dto.MoveDto;
import chess.dto.ScoreDto;
import chess.dto.WinnerDto;
import chess.service.ChessGameService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChessSpringController {

    private final ChessGameService chessGameService;

    public ChessSpringController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public String enter(Model model) {
        List<GameDto> gameDtos = chessGameService.find();
        model.addAttribute("games", gameDtos);
        return "home";
    }

    @PostMapping("/game")
    public String create(@ModelAttribute GameDto gameDto) {
        chessGameService.create(gameDto);
        return "redirect:/";
    }

    @GetMapping("/game/{gameId}")
    public String enter(@PathVariable String gameId) {
        return "index";
    }

    @DeleteMapping("/game/{gameId}")
    public String delete(@RequestParam("password") String password, @PathVariable int gameId, Model model) {
        try {
            chessGameService.delete(gameId, password);
        } catch (IllegalArgumentException exception) {
            model.addAttribute("value", exception.getMessage());
            model.addAttribute("games", chessGameService.find());
            return "home";
        }
        return "redirect:/";
    }

    @PutMapping("/game/{gameId}/start")
    public ResponseEntity<GameStatusDto> start(@PathVariable int gameId) {
        GameStatusDto gameStatusDto = chessGameService.startChessGame(new WebBasicBoardStrategy(), gameId);
        return ResponseEntity.ok().body(gameStatusDto);
    }

    @PostMapping("/game/{gameId}/move")
    public ResponseEntity<GameStatusDto> move(@RequestBody MoveDto moveDto, @PathVariable int gameId) {
        GameStatusDto gameStatusDto = chessGameService.move(moveDto.getFrom(), moveDto.getTo(), gameId);
        return ResponseEntity.ok().body(gameStatusDto);
    }

    @GetMapping("/game/{gameId}/status")
    public ResponseEntity<ScoreDto> status(@PathVariable int gameId) {
        ScoreDto scoresDto = chessGameService.createScore(gameId);
        return ResponseEntity.ok().body(scoresDto);
    }

    @PutMapping("/game/{gameId}/end")
    public ResponseEntity<WinnerDto> end(@PathVariable int gameId) {
        WinnerDto winnerDto = chessGameService.end(gameId);
        return ResponseEntity.ok().body(winnerDto);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handle(IllegalArgumentException illegalArgumentException) {
        return ResponseEntity.internalServerError().body(new ErrorDto(illegalArgumentException.getMessage()));
    }
}
