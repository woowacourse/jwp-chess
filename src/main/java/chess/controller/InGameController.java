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
        if (isGameExist(chessGameDto) && isNotValidPassword(chessGameDto)) {
            model.addAttribute("msg", "비밀 번호 틀렸지롱~ 🤪");
            model.addAttribute("games", chessService.getGameIDs());
            return "ready";
        }
        ChessGame chessGame = chessService.loadChessGame(chessGameDto, restart);
        GameResult gameResult = chessService.getGameResult(chessGameDto);
        addGameInfo(chessGameDto, model, gameResult);

        model.addAllAttributes(chessGame.getEmojis());
        model.addAttribute("msg", "누가 이기나 보자구~!");
        return "ingame";
    }

    private boolean isGameExist(ChessGameDto chessGameDto) {
        return chessService.isGameExist(chessGameDto);
    }

    private boolean isNotValidPassword(ChessGameDto chessGameDto) {
        return !chessService.isValidPassword(chessGameDto);
    }

    private void addGameInfo(@ModelAttribute ChessGameDto chessGameDto,
            Model model, GameResult gameResult) {
        model.addAttribute("whiteScore", gameResult.calculateScore(Color.WHITE));
        model.addAttribute("blackScore", gameResult.calculateScore(Color.BLACK));
        model.addAttribute("gameID", chessGameDto.getGameID());
    }

    @PostMapping(value = "/{gameID}/move")
    public String movePiece(@ModelAttribute ChessGameDto chessGameDto, @ModelAttribute MovementRequest movement,
            Model model) {
        ChessGame chessGame = chessService.loadSavedChessGame(chessGameDto);
        executeMove(chessGameDto, model, chessGame, movement);

        GameResult gameResult = chessService.getGameResult(chessGameDto);
        addGameInfo(chessGameDto, model, gameResult);

        if (chessGame.isKingDie()) {
            model.addAttribute("msg", "킹 잡았다!! 게임 끝~!~!");
            return "finished";
        }

        return "ingame";
    }

    private void executeMove(ChessGameDto chessGameDto, Model model, ChessGame chessGame, @ModelAttribute MovementRequest movement) {
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
    }
}
