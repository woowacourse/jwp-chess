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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @PostMapping("/games/{gameId}/pieces")
    public ResponseEntity<ChessGameDto> startGame(@PathVariable int gameId) {
        return new ResponseEntity<>(chessService.newGame(gameId), HttpStatus.CREATED);
    }

    @GetMapping("/games/{gameId}/pieces")
    public ResponseEntity<ChessGameDto> restart(@PathVariable int gameId) {
        return new ResponseEntity<>(chessService.loadGame(gameId), HttpStatus.OK);
    }

    @PatchMapping(value = "/games/{gameId}/pieces", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChessGameDto> move(@PathVariable int gameId, @RequestBody MoveDto moveDto) {
        //rest api 적용하면서 생긴 로직
        moveDto.setGameId(gameId);
        return new ResponseEntity<>(chessService.move(moveDto), HttpStatus.OK);
    }

    @GetMapping("/games/{gameId}")
    public String getGame(@PathVariable int gameId, Model model) {
        model.addAttribute("gameId", gameId);
        return "board";
    }

    @DeleteMapping("/games/{gameId}")
    public ResponseEntity<Void> deleteGame(@PathVariable int gameId, @RequestBody GameRoomDto gameRoomDto) {
        gameRoomDto.setGameId(gameId);
        chessService.deleteGame(gameRoomDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/games", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createGame(@ModelAttribute GameRoomDto gameRoomDto) {
        var gameId = chessService.createRoom(gameRoomDto);
        return "redirect:/games/" + gameId;
    }

    @ExceptionHandler({RuntimeException.class})
    private ResponseEntity<String> handleException(final RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
