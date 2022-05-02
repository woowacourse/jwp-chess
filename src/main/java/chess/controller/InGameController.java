package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import chess.dto.ChessGameRequest;
import chess.domain.piece.Color;
import chess.dto.MovementRequest;
import chess.service.ChessService;

@Controller
@RequestMapping("/games")
public class InGameController {
    private final ChessService chessService;

    public InGameController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping
    public String runGame(@ModelAttribute ChessGameRequest chessGameRequest, @RequestParam(name = "restart") String restart,
            Model model) {
        if (isGameExist(chessGameRequest) && isNotValidPassword(chessGameRequest)) {
            model.addAttribute("msg", "비밀 번호 틀렸지롱~ 🤪");
            model.addAttribute("games", chessService.getGameIds());
            return "ready";
        }
        addScores(model, chessGameRequest);

        model.addAllAttributes(chessService.getEmojis(chessGameRequest, restart));
        model.addAttribute("msg", "누가 이기나 보자구~!");
        return "ingame";
    }

    private boolean isGameExist(ChessGameRequest chessGameRequest) {
        return chessService.isGameExist(chessGameRequest);
    }

    private boolean isNotValidPassword(ChessGameRequest chessGameRequest) {
        return !chessService.isValidPassword(chessGameRequest);
    }

    private void addScores(Model model, ChessGameRequest chessGameRequest) {
        model.addAttribute("whiteScore", chessService.calculateScore(chessGameRequest, Color.WHITE));
        model.addAttribute("blackScore", chessService.calculateScore(chessGameRequest, Color.BLACK));
    }

    @PostMapping(value = "/{gameId}/move")
    public String movePiece(@ModelAttribute ChessGameRequest chessGameRequest, @ModelAttribute MovementRequest movement,
            Model model) {
        executeMove(chessGameRequest, model, movement);
        addScores(model, chessGameRequest);

        if (chessService.isKingDie(chessGameRequest)) {
            model.addAttribute("msg", "킹 잡았다!! 게임 끝~!~!");
            return "finished";
        }

        return "ingame";
    }

    private void executeMove(ChessGameRequest chessGameRequest, Model model, @ModelAttribute MovementRequest movementRequest) {
        try {
            chessService.movePiece(chessGameRequest, movementRequest);
            model.addAllAttributes(chessService.getSavedEmojis(chessGameRequest));
            model.addAttribute("msg", "누가 이기나 보자구~!");
        } catch (IllegalArgumentException e) {
            model.addAllAttributes(chessService.getSavedEmojis(chessGameRequest));
            model.addAttribute("msg", e.getMessage());
        }
    }
}
