package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import chess.dto.ChessGameDto;
import chess.domain.ChessGame;
import chess.domain.GameResult;
import chess.domain.piece.Color;
import chess.domain.position.Square;
import chess.dto.MovementRequest;
import chess.service.ChessService;

@Controller
@RequestMapping("/games")
public class InGameController {
    private final ChessService chessService;

    public InGameController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping()
    public String runGame(@ModelAttribute ChessGameDto chessGameDto, @RequestParam(name = "restart") String restart,
            Model model) {
        if (chessService.isGameExist(chessGameDto) && !chessService.isValidPassword(chessGameDto)) {
            model.addAttribute("msg", "비밀 번호 틀렸지롱~ 🤪");
            model.addAttribute("games", chessService.getGameIDs());
            return "ready";
        }
        ChessGame chessGame = chessService.loadChessGame(chessGameDto, restart);
        GameResult gameResult = chessService.getGameResult(chessGameDto);

        model.addAttribute("whiteScore", gameResult.calculateScore(Color.WHITE));
        model.addAttribute("blackScore", gameResult.calculateScore(Color.BLACK));

        model.addAllAttributes(chessGame.getEmojis());
        model.addAttribute("msg", "누가 이기나 보자구~!");
        model.addAttribute("gameID", chessGameDto.getGameID());
        return "ingame";
    }

    @PostMapping(value = "/{gameID}/move")
    public String movePiece(@ModelAttribute ChessGameDto chessGameDto, @ModelAttribute MovementRequest movement,
            Model model) {
        ChessGame chessGame = chessService.loadSavedChessGame(chessGameDto);
        String source = movement.getSource();
        String target = movement.getTarget();

        try {
            chessGame.move(new Square(source), new Square(target));
            chessService.movePiece(chessGameDto, chessGame, source, target);
            model.addAllAttributes(chessGame.getEmojis());
            model.addAttribute("msg", "누가 이기나 보자구~!");
        } catch (IllegalArgumentException e) {
            model.addAllAttributes(chessGame.getEmojis());
            model.addAttribute("msg", e.getMessage());
        }
        model.addAttribute("gameID", chessGameDto.getGameID());
        GameResult gameResult = chessService.getGameResult(chessGameDto);
        model.addAttribute("whiteScore", gameResult.calculateScore(Color.WHITE));
        model.addAttribute("blackScore", gameResult.calculateScore(Color.BLACK));

        if (chessGame.isKingDie()) {
            model.addAttribute("msg", "킹 잡았다!! 게임 끝~!~!");
            return "finished";
        }

        return "ingame";
    }
}
