package chess.controller;

import chess.domain.board.ChessGame;
import chess.domain.piece.property.Team;
import chess.dto.BoardResponse;
import chess.dto.GameRoomResponse;
import chess.dto.GameCreationRequest;
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
        long gameId = chessService.addChessGame(gameCreationRequest);
        return "redirect:/chess/game/" + gameId;
    }

    @GetMapping("/chess/game/{id}")
    public String showChessGameRoom(@PathVariable long id, Model model) {
        GameRoomResponse chessGameRoomInfoDTO = chessService.findGameById(id);
        model.addAttribute("chessGameRoom", chessGameRoomInfoDTO);
        return "game";
    }

    @GetMapping("/chess/game/{id}/board")
    @ResponseBody
    public ResponseEntity<BoardResponse> createBoard(@PathVariable long id) {
        ChessGame chessGame = chessService.loadSavedGame(id);
        return ResponseEntity.ok(new BoardResponse(chessGame));
    }

    @PostMapping("/chess/game/{id}/move")
    @ResponseBody
    public ResponseEntity<BoardResponse> movePiece(@RequestParam String source,
                                                   @RequestParam String target,
                                                   @RequestParam String team,
                                                   @PathVariable long id) {
        ChessGame chessGame = chessService.movePiece(id, source, target, Team.valueOf(team));
        return ResponseEntity.ok(new BoardResponse(chessGame));
    }

    @DeleteMapping(value = "/chess/{id}")
    public ResponseEntity<Object> deleteGame(@PathVariable long id, String password) {
        chessService.deleteGame(id, password);
        return ResponseEntity.noContent().build();
    }
}
