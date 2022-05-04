package chess.controller;

import chess.dto.BoardResponse;
import chess.dto.GameCreationRequest;
import chess.dto.GameRoomResponse;
import chess.dto.MoveRequest;
import chess.service.ChessService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChessGameController {

    private final ChessService chessService;

    public ChessGameController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String showRooms(Model model) {
        model.addAttribute("games", chessService.getGames());
        return "lobby";
    }

    @PostMapping(value = "/chess/new", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createGame(@ModelAttribute GameCreationRequest gameCreationRequest) {
        chessService.addChessGame(gameCreationRequest);
        return "redirect:/";
    }

    @PostMapping("/chess/game/{id}")
    public String showChessGameRoom(@PathVariable long id, @RequestParam String password, Model model) {
        GameRoomResponse chessGameRoomResponse = chessService.findRoomToEnter(id, password);
        model.addAttribute("chessGameRoom", chessGameRoomResponse);
        return "game";
    }

    @GetMapping("/chess/game/{id}/board")
    @ResponseBody
    public ResponseEntity<BoardResponse> loadSavedGame(@PathVariable long id) {
        BoardResponse boardResponse = chessService.loadSavedBoard(id);
        return ResponseEntity.ok(boardResponse);
    }

    @PostMapping("/chess/game/{id}/move")
    @ResponseBody
    public ResponseEntity<BoardResponse> movePiece(@ModelAttribute MoveRequest moveRequest, @PathVariable long id) {
        BoardResponse boardResponse = chessService.movePiece(id, moveRequest);
        return ResponseEntity.ok(boardResponse);
    }

    @DeleteMapping(value = "/chess/{id}")
    public ResponseEntity<Object> deleteGame(@PathVariable long id, String password) {
        chessService.deleteGame(id, password);
        return ResponseEntity.noContent().build();
    }
}
