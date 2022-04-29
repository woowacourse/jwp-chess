package chess.controller;

import chess.dto.ChessGameDto;
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
        ChessGameDto chessGameDto = chessGameService.getOrSaveChessGame(id);
        model.addAttribute("pieces", chessGameService.findPieces(chessGameDto.getId()));
        model.addAttribute("chessGame", chessGameDto);
        return "chess-game";
    }

    @PostMapping("/chess-game/move")
    public String move(@ModelAttribute MoveRequest moveRequest, RedirectAttributes attributes) {
        ChessGameDto chessGameDto = chessGameService.move(moveRequest);
        if (chessGameDto.getStatus().isFinished()) {
            attributes.addFlashAttribute("isFinished", true);
            attributes.addFlashAttribute("winner", chessGameDto.getWinner());
        }
        return "redirect:/chess-game?id=" + chessGameDto.getId();
    }

    @DeleteMapping("/chess-game/{id}")
    public String delete(@PathVariable("id") int chessGameId, @RequestHeader(value = "Authorization") String password) {
        chessGameService.deleteRoom(chessGameId, password);
        return "redirect:/";
    }
}
