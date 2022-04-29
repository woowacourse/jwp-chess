package chess.controller;

import chess.dto.ChessGameDto;
import chess.dto.ChessGameRequest;
import chess.dto.MoveRequest;
import chess.service.ChessGameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ChessGameController {

    private final ChessGameService chessGameService;

    public ChessGameController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/chess-game")
    public String chessGame(@RequestParam int id, Model model) {
        ChessGameDto chessGameDto = chessGameService.findChessGame(id);
        model.addAttribute("pieces", chessGameService.findPieces(chessGameDto.getId()));
        model.addAttribute("chessGame", chessGameDto);
        return "chess-game";
    }

    @PostMapping("/chess-game")
    public String createChessGame(@ModelAttribute ChessGameRequest chessGameRequest) {
        chessGameService.create(chessGameRequest);
        return "redirect:/";
    }

    @PostMapping("/chess-game/move")
    public String move(@ModelAttribute MoveRequest moveRequest, RedirectAttributes attributes) {
        int chessGameId = chessGameService.move(moveRequest);
        if (chessGameService.isFinished(chessGameId)) {
            attributes.addFlashAttribute("isFinished", true);
            attributes.addFlashAttribute("winner", chessGameService.findWinner(chessGameId));
        }
        return "redirect:/chess-game?id=" + chessGameId;
    }

    @DeleteMapping("/chess-game/{id}")
    public String delete(@PathVariable("id") int chessGameId, @RequestHeader(value = "Authorization") String password) {
        chessGameService.deleteRoom(chessGameId, password);
        return "redirect:/";
    }
}
