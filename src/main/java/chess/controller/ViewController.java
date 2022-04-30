package chess.controller;

import chess.controller.view.BoardView;
import chess.dto.PiecesDto;
import chess.service.ChessGameService;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewController {
    private static final String LOCALHOST_8080 = "http://localhost:8080";

    private final ChessGameService chessGameService;

    public ViewController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public ModelAndView toIndex(@RequestParam(required = false, defaultValue = "") String gameMessage) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("gameMessage", gameMessage);
        modelAndView.addObject("rooms", chessGameService.getRooms());
        return modelAndView;
    }

    @GetMapping(path = "/game/{id}")
    public ModelAndView toGame(@PathVariable String id) {
        chessGameService.getGameStatus(id);
        ModelAndView modelAndView = new ModelAndView("game");
        modelAndView.addObject("pieces", BoardView.of(new PiecesDto(chessGameService.getPieces(id))).getBoardView());
        modelAndView.addObject("id", id);
        modelAndView.addObject("status", chessGameService.calculateGameResult(id));
        return modelAndView;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<Void> exception(Exception e) {
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create(LOCALHOST_8080 + "?gameMessage=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8)))
                .build();
    }
}
