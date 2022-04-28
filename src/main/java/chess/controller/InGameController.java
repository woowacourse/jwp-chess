package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import chess.ChessGameVO;
import chess.domain.ChessGame;
import chess.domain.GameResult;
import chess.domain.piece.Color;
import chess.domain.position.Square;
import chess.service.ChessService;

@Controller
@RequestMapping("/games")
public class InGameController {
    private final ChessService chessService;

    public InGameController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping()
    public String runGame(@ModelAttribute ChessGameVO chessGameVO, @RequestParam(name = "restart") String restart,
            Model model) {
        System.err.println(chessGameVO.getGameID());
        System.err.println(chessGameVO.getPassword());
        ChessGame chessGame = chessService.loadChessGame(chessGameVO, restart);
        GameResult gameResult = chessService.getGameResult(chessGameVO);

        model.addAttribute("whiteScore", gameResult.calculateScore( Color.WHITE));
        model.addAttribute("blackScore", gameResult.calculateScore(Color.BLACK));

        model.addAllAttributes(chessGame.getEmojis());
        model.addAttribute("msg", "누가 이기나 보자구~!");
        model.addAttribute("gameID", chessGameVO.getGameID());
        return "ingame";
    }

    @PostMapping(value = "/{gameID}")
    public String movePiece(@ModelAttribute ChessGameVO chessGameVO, @ModelAttribute MovementRequest movement, Model model) {
        ChessGame chessGame = chessService.loadSavedChessGame(chessGameVO);
        String source = movement.getSource();
        String target = movement.getTarget();

        try {
            chessGame.move(new Square(source), new Square(target));
            chessService.movePiece(chessGameVO, chessGame, source, target);
            model.addAllAttributes(chessGame.getEmojis());
            model.addAttribute("msg", "누가 이기나 보자구~!");
        } catch (IllegalArgumentException e) {
            model.addAllAttributes(chessGame.getEmojis());
            model.addAttribute("msg", e.getMessage());
        }
        model.addAttribute("gameID", chessGameVO.getGameID());
        GameResult gameResult = chessService.getGameResult(chessGameVO);
        model.addAttribute("whiteScore", gameResult.calculateScore(Color.WHITE));
        model.addAttribute("blackScore", gameResult.calculateScore(Color.BLACK));

        if (chessGame.isKingDie()) {
            model.addAttribute("msg", "킹 잡았다!! 게임 끝~!~!");
            return "finished";
        }

        return "ingame";
    }
}
