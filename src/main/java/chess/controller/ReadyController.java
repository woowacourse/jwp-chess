package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import chess.ChessGameVO;
import chess.service.ChessService;

@Controller
public class ReadyController {
    private final ChessService chessService;

    public ReadyController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("msg", "CLICK TO START! 😝");
        model.addAttribute("games", chessService.getGameIDs());
        return "ready";
    }

    @PostMapping("/")
    public String delete(@ModelAttribute ChessGameVO chessGame, Model model) {
        model.addAttribute("msg", "쨘~ 게임 삭제 완료! 😚");
        try {
            chessService.deleteGameByGameID(chessGame.getGameID(), chessGame.getPassword());
        } catch (IllegalArgumentException e) {
            model.addAttribute("msg", e.getMessage());
        }
        model.addAttribute("games", chessService.getGameIDs());
        return "ready";
    }
}
