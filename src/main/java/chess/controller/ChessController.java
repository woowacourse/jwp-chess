package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import chess.dto.MovementRequest;
import chess.service.ChessService;

@Controller
public class ChessController {
    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("msg", "CLICK TO START! 😝");
        model.addAttribute("games", chessService.getGameIds());
        return "ready";
    }

    @PostMapping("/delete/{gameId}")
    public String delete(@PathVariable String gameId, @RequestParam String password, Model model) {
        model.addAttribute("msg", "쨘~ 게임 삭제 완료! 😚");
        try {
            chessService.deleteGameByGameId(gameId, password);
        } catch (IllegalArgumentException e) {
            model.addAttribute("msg", e.getMessage());
        }
        model.addAttribute("games", chessService.getGameIds());
        return "ready";
    }

    @PostMapping("/games")
    public String runGame(@RequestParam String gameId, @RequestParam String password,
            @RequestParam(name = "restart") String restart, Model model) {
        if (isGameExist(gameId) && isNotValidPassword(gameId, password)) {
            model.addAttribute("msg", "비밀 번호 틀렸지롱~ 🤪");
            model.addAttribute("games", chessService.getGameIds());
            return "ready";
        }
        addScores(model, gameId);

        model.addAttribute("gameId", gameId);
        model.addAttribute("password", password);
        model.addAllAttributes(chessService.getEmojis(gameId, password, restart));
        model.addAttribute("msg", "누가 이기나 보자구~!");
        return "ingame";
    }

    private boolean isGameExist(String gameId) {
        return chessService.isGameExist(gameId);
    }

    private boolean isNotValidPassword(String gameId, String password) {
        return !chessService.isValidPassword(gameId, password);
    }

    private void addScores(Model model, String gameId) {
        model.addAttribute("score", chessService.calculateScore(gameId));
    }

    @PostMapping(value = "/games/{gameId}")
    public String movePiece(@PathVariable String gameId, @ModelAttribute MovementRequest movement, Model model) {
        model.addAttribute("gameId", gameId);
        executeMove(gameId, model, movement);
        addScores(model, gameId);

        if (chessService.isKingDie(gameId)) {
            model.addAttribute("msg", "킹 잡았다!! 게임 끝~!~!");
            return "finished";
        }

        return "ingame";
    }

    private void executeMove(String gameId, Model model, @ModelAttribute MovementRequest movementRequest) {
        try {
            chessService.movePiece(gameId, movementRequest);
            model.addAllAttributes(chessService.getSavedEmojis(gameId));
            model.addAttribute("msg", "누가 이기나 보자구~!");
        } catch (IllegalArgumentException e) {
            model.addAllAttributes(chessService.getSavedEmojis(gameId));
            model.addAttribute("msg", e.getMessage());
        }
    }

    @GetMapping("/results")
    public String showResult(@RequestParam String gameId, Model model) {
        model.addAttribute("gameId", gameId);
        model.addAttribute("score", chessService.calculateScore(gameId));
        return "status";
    }
}
