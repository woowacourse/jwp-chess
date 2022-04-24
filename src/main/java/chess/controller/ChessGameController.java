package chess.controller;

import chess.dao.ChessGame;
import chess.domain.piece.property.Team;
import chess.dto.BoardDTO;
import chess.dto.ChessGameRoomInfoDTO;
import chess.service.ChessService;
import java.sql.SQLException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PostMapping("/chess/new")
    public String createGame(@RequestParam String gameName) {
        String gameId = chessService.addChessGame(gameName);
        return "redirect:/chess/game/" + gameId;
    }

    @GetMapping("/chess/game/{id}")
    public String showChessGameRoom(@PathVariable String id, Model model) {
        ChessGameRoomInfoDTO chessGameRoomInfoDTO = chessService.findGameById(id);
        model.addAttribute("chessGameRoom", chessGameRoomInfoDTO);
        return "game";
    }

    @GetMapping("/chess/game/{id}/board")
    @ResponseBody
    public ResponseEntity<BoardDTO> createBoard(@PathVariable String id) {
        ChessGame chessGame = chessService.getChessGamePlayed(id);
        return ResponseEntity.ok(new BoardDTO(chessGame));
    }

    @PostMapping("/chess/game/{id}/move")
    @ResponseBody
    public ResponseEntity<BoardDTO> movePiece(@RequestParam String source,
                                              @RequestParam String target,
                                              @RequestParam String team,
                                              @PathVariable String id) throws SQLException {
        ChessGame chessGame = chessService.movePiece(id, source, target, Team.valueOf(team));
        return ResponseEntity.ok(new BoardDTO(chessGame));
    }
}
