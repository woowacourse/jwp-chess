package chess.controller;

import chess.dto.ChessGameDto;
import chess.dto.MoveRequest;
import chess.dto.RoomCreateRequest;
import chess.service.ChessGameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ChessGameController {

    private final ChessGameService chessGameService;

    public ChessGameController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/chess-game/{id}")
    public String chessGame(@PathVariable int id, Model model) {
        model.addAttribute("chessGame", chessGameService.findChessGame(id));
        model.addAttribute("pieces", chessGameService.findPieces(id));
        return "chess-game";
    }

    @PostMapping("/chess-game")
    public String createChessGame(@ModelAttribute RoomCreateRequest roomCreateRequest) {
        chessGameService.create(roomCreateRequest);
        return "redirect:/";
    }

    @PostMapping("/chess-game/move")
    public String move(@ModelAttribute MoveRequest moveRequest, RedirectAttributes attributes) {
        ChessGameDto chessGameDto = chessGameService.move(moveRequest);
        attributes.addFlashAttribute("isFinished", chessGameDto.getStatus().isFinished());
        attributes.addFlashAttribute("winner", chessGameDto.getWinner());
        return "redirect:/chess-game/" + chessGameDto.getId();
    }

    @DeleteMapping("/chess-game/{id}")
    public String delete(@PathVariable("id") int chessGameId, @RequestHeader(value = "Authorization") String password) {
        chessGameService.deleteRoom(chessGameId, password);
        return "redirect:/";
    }
}
