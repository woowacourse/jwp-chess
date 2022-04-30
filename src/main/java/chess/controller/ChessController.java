package chess.controller;

import chess.controller.view.BoardView;
import chess.dto.LogInDto;
import chess.dto.PiecesDto;
import chess.service.ChessGameService;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChessController {
    private static final String LOCALHOST_8080 = "http://localhost:8080";

    private final ChessGameService chessGameService;

    public ChessController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public ModelAndView index(@RequestParam(required = false, defaultValue = "") String msg) {
        return getIndexModel(msg);
    }

    @PostMapping("/init")
    @ResponseBody
    public ResponseEntity<Void> initGame(@ModelAttribute LogInDto logInDto) {
        System.err.println("게임생성됨" + logInDto);
        chessGameService.createGame(logInDto);
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create(LOCALHOST_8080 + "/game/" + logInDto.getId())).build();
    }

    @PostMapping("/start")
    @ResponseBody
    public ResponseEntity<Void> startGame(@ModelAttribute LogInDto logInDto) {
        chessGameService.validateLogIn(logInDto);
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create(LOCALHOST_8080 + "/game/" + logInDto.getId())).build();
    }

    @PostMapping("/exit")
    public ResponseEntity<Void> deleteGameAndGoHome(@ModelAttribute LogInDto logInDto) {
        chessGameService.validateEnd(logInDto.getId());
        chessGameService.cleanGame(logInDto);
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create(LOCALHOST_8080)).build();
    }

    @GetMapping(path = "/game/{id}")
    public ModelAndView getGame(@PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView("game");
        modelAndView.addObject("pieces", BoardView.of(new PiecesDto(chessGameService.getPieces(id))).getBoardView());
        modelAndView.addObject("id", id);
        modelAndView.addObject("status", chessGameService.calculateGameResult(id));
        return modelAndView;
    }

    private ModelAndView getIndexModel(String gameMessage) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("gameMessage", gameMessage);
        modelAndView.addObject("rooms", chessGameService.getRooms());
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView exception(Exception e) {
        return getIndexModel(e.getMessage());
    }
}
