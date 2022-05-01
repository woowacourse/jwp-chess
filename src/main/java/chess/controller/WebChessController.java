package chess.controller;

import chess.converter.Converter;
import chess.domain.board.Board;
import chess.domain.game.StatusCalculator;
import chess.dto.ExceptionDto;
import chess.dto.GameCreateDto;
import chess.dto.GameDeleteDto;
import chess.dto.GameDeleteResponseDto;
import chess.dto.GameDto;
import chess.dto.MoveRequestDto;
import chess.service.ChessService;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebChessController {

    private final ChessService chessService;

    public WebChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/game/{id}")
    public ModelAndView game(@PathVariable("id") int id) {
        Board board = chessService.findBoardByGameId(id);
        StatusCalculator status = chessService.createStatus(id);

        Map<String, Object> map = Converter.toMap(id, board, status);
        ModelAndView modelAndView = new ModelAndView("start");
        modelAndView.addObject("board", map);
        return modelAndView;
    }

    @GetMapping("/game")
    public ResponseEntity<List<GameDto>> findGames() {
        return ResponseEntity.ok().body(chessService.findGames());
    }

    @PostMapping("/game")
    public String create(@ModelAttribute GameCreateDto gameCreateDto) {
        int gameId = chessService.start(gameCreateDto);
        return "redirect:/game/" + gameId;
    }

    @PutMapping("/game/{gameId}/move")
    @ResponseBody
    public ResponseEntity<MoveRequestDto> move(
            @PathVariable("gameId") int gameId, @RequestBody MoveRequestDto moveRequestDto) {
        chessService.move(gameId, moveRequestDto);
        return ResponseEntity.ok(moveRequestDto);
    }

    @PostMapping("/home")
    public String stop() {
        return "redirect:/";
    }

    @DeleteMapping("/game")
    public ResponseEntity<GameDeleteResponseDto> delete(@RequestBody GameDeleteDto gameDeleteDto) {
        return ResponseEntity.ok().body(chessService.deleteGameByGameId(gameDeleteDto));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDto> handle(Exception e) {
        return ResponseEntity.badRequest().body(new ExceptionDto(e.getMessage()));
    }
}
