package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import chess.dto.ChessGameDto;
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
    public String runGame(@ModelAttribute ChessGameDto chessGameDto, @RequestParam(name = "restart") String restart,
            Model model) {
        if (isGameExist(chessGameDto) && isNotValidPassword(chessGameDto)) {
            model.addAttribute("msg", "비밀 번호 틀렸지롱~ 🤪");
            model.addAttribute("games", chessService.getGameIds());
            return "ready";
        }
        addScores(model, chessGameDto);

        model.addAllAttributes(chessService.getEmojis(chessGameDto, restart));
        model.addAttribute("msg", "누가 이기나 보자구~!");
        return "ingame";
    }

    private boolean isGameExist(ChessGameDto chessGameDto) {
        return chessService.isGameExist(chessGameDto);
    }

    private boolean isNotValidPassword(ChessGameDto chessGameDto) {
        return !chessService.isValidPassword(chessGameDto);
    }

    private void addScores(Model model, ChessGameDto chessGameDto) {
        model.addAttribute("whiteScore", chessService.calculateScore(chessGameDto, Color.WHITE));
        model.addAttribute("blackScore", chessService.calculateScore(chessGameDto, Color.BLACK));
    }

    @PostMapping(value = "/{gameId}/move")
    public String movePiece(@ModelAttribute ChessGameDto chessGameDto, @ModelAttribute MovementRequest movement,
            Model model) {
        executeMove(chessGameDto, model, movement);
        addScores(model, chessGameDto);

        if (chessService.isKingDie(chessGameDto)) {
            model.addAttribute("msg", "킹 잡았다!! 게임 끝~!~!");
            return "finished";
        }

        return "ingame";
    }

    private void executeMove(ChessGameDto chessGameDto, Model model, @ModelAttribute MovementRequest movementRequest) {
        try {
            chessService.movePiece(chessGameDto, movementRequest);
            model.addAllAttributes(chessService.getSavedEmojis(chessGameDto));
            model.addAttribute("msg", "누가 이기나 보자구~!");
        } catch (IllegalArgumentException e) {
            model.addAllAttributes(chessService.getSavedEmojis(chessGameDto));
            model.addAttribute("msg", e.getMessage());
        }
    }
}
