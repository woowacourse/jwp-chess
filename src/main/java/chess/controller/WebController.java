package chess.controller;

import chess.dto.ChessGameDto;
import chess.dto.GameRoomDto;
import chess.dto.MoveDto;
import chess.service.ChessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    private final ChessService chessService;

    public WebController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String indexPage(Model model) {
        model.addAttribute("games", chessService.findAllGame());
        return "index";
    }

    @GetMapping("/start")
    public ResponseEntity<ChessGameDto> startGame(@RequestParam int gameId) {
        return new ResponseEntity<>(chessService.newGame(gameId), HttpStatus.CREATED);
    }

    @GetMapping("/restart")
    public ResponseEntity<ChessGameDto> restart(@RequestParam int gameId) {
        return new ResponseEntity<>(chessService.loadGame(gameId), HttpStatus.OK);
    }

    @PatchMapping(value = "/move", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChessGameDto> move(@RequestBody MoveDto moveDto) {
        return new ResponseEntity<>(chessService.move(moveDto), HttpStatus.OK);
    }

    @GetMapping("/game")
    public String getGame(@RequestParam int gameId, Model model) {
        model.addAttribute("gameId", gameId);
        return "game";
    }

    @DeleteMapping("/game")
    public ResponseEntity<Void> deleteGame(@RequestBody GameRoomDto gameRoomDto) {
        chessService.deleteGame(gameRoomDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/game", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createGame(@ModelAttribute GameRoomDto gameRoomDto) {
        var gameId = chessService.createRoom(gameRoomDto);
        return "redirect:/game?gameId=" + gameId;
    }

    @ExceptionHandler({RuntimeException.class})
    private ResponseEntity<String> handleException(final RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
