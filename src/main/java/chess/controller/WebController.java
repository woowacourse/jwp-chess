package chess.controller;

import chess.dto.ChessGameDto;
import chess.dto.DeleteResponse;
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
        return "index2";
    }

    @GetMapping("/start")
    public ResponseEntity<ChessGameDto> startGame(@RequestParam int gameId) {
        chessService.newGame(gameId);
        return new ResponseEntity<>(chessService.newGame(gameId), HttpStatus.CREATED);
    }

    @GetMapping("/restart")
    public ResponseEntity<ChessGameDto> restart(@RequestParam int gameId) {
        return new ResponseEntity<>(chessService.loadGame(gameId), HttpStatus.ACCEPTED);
    }

    @PutMapping("/move")
    public ResponseEntity<ChessGameDto> move(@RequestBody MoveDto moveDto) {
        return new ResponseEntity<>(chessService.move(moveDto), HttpStatus.ACCEPTED);
    }

    @GetMapping("/game")
    public String getGame(@RequestParam int gameId, Model model) {
        model.addAttribute("gameId", gameId);
        return "index";
    }

    @DeleteMapping("/game")
    @ResponseBody
    public ResponseEntity<DeleteResponse> deleteGame(@RequestBody GameRoomDto gameRoomDto) {
        chessService.deleteGame(gameRoomDto);
        return new ResponseEntity<>(new DeleteResponse(true), HttpStatus.OK);
    }

    @PostMapping(value = "/game", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createGame(@ModelAttribute GameRoomDto gameRoomDto) {
        Number gameId = chessService.createRoom(gameRoomDto);
        return "redirect:/game?gameId=" + gameId;
    }

    @ExceptionHandler({IllegalArgumentException.class})
    private ResponseEntity<String> handleException(final IllegalArgumentException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
