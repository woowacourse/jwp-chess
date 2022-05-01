package chess.controller;

import chess.domain.game.ChessGame;
import chess.domain.game.GameResult;
import chess.domain.piece.Color;
import chess.domain.position.Movement;
import chess.domain.position.Square;
import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/ingame")
public class InGameController {
    private final ChessService chessService;

    public InGameController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping()
    public String runGame(@RequestParam String gameCode, Model model) {
        final String gameID = chessService.findGameID(gameCode);
        ChessGame chessGame = chessService.loadGame(gameID);
        chessService.startGame(gameID, chessGame);
        chessService.loadPieces(gameID);
        model.addAllAttributes(chessGame.getEmojis());
        GameResult gameResult = chessService.getGameResult(gameID);
        model.addAttribute("whiteScore", gameResult.calculateScore(Color.WHITE));
        model.addAttribute("blackScore", gameResult.calculateScore(Color.BLACK));

        if (chessService.isFinished(gameID)) {
            model.addAttribute("msg", "킹 잡았다!! 게임 끝~!~!");
            return "finished";
        }

        model.addAttribute("turn", chessService.getTurn(gameID));
        model.addAttribute("msg", "누가 이기나 보자구~!");
        model.addAttribute("gameCode", gameCode);
        return "ingame";
    }

    @PostMapping("/{gameCode}")
    public String movePiece(@PathVariable String gameCode, @RequestBody String movement, Model model) {
        String gameID = chessService.findGameID(gameCode);
        ChessGame chessGame = chessService.loadSavedChessGame(gameID, chessService.getTurn(gameID));

        List<String> movements = Arrays.asList(movement.split("&"));
        String source = getPosition(movements.get(0));
        String target = getPosition(movements.get(1));

        doCastlingOrMove(gameID, chessGame, source, target, model);

        model.addAllAttributes(chessGame.getEmojis());
        model.addAttribute("gameCode", gameCode);

        GameResult gameResult = chessService.getGameResult(gameID);
        model.addAttribute("turn", chessService.getTurn(gameID));
        model.addAttribute("whiteScore", gameResult.calculateScore(Color.WHITE));
        model.addAttribute("blackScore", gameResult.calculateScore(Color.BLACK));

        if (chessGame.isKingDie()) {
            model.addAttribute("msg", "킹 잡았다!! 게임 끝~!~!");
            return "finished";
        }
        return "ingame";
    }

    private void doCastlingOrMove(String gameID, ChessGame chessGame, String source, String target, Model model){
        if(chessGame.isCastable(new Square(source), new Square(target))){
            doCastling(gameID, chessGame, source, target, model);
            return;
        }
        doMove(gameID, chessGame, source, target, model);
    }

    private void doCastling(String gameID, ChessGame chessGame, String source, String target, Model model) {
        chessGame.doCastling(new Square(source), new Square(target));

        Square sourceSquare = new Square(source);
        Square targetSquare = new Square(target);

        boolean isQueenSide = sourceSquare.isPlacedOnRightSideOf(targetSquare);

        String movedSource = sourceSquare.add(sourceCastlingMovement(isQueenSide)).getName();
        String movedTarget = targetSquare.add(targetCastlingMovement(isQueenSide)).getName();

        chessService.movePiece(gameID, source, movedSource);
        chessService.movePiece(gameID, target, movedTarget);
        chessService.updateTurn(gameID, chessGame);
        model.addAttribute("msg", "누가 이기나 보자구~!");
    }

    private Movement sourceCastlingMovement(boolean isQueenSide) {
        if(isQueenSide){
            return new Movement(-2,0);
        }
        return new Movement(2,0);
    }

    private Movement targetCastlingMovement(boolean isQueenSide) {
        if(isQueenSide){
            return new Movement(3,0);
        }
        return new Movement(-2,0);
    }

    private void doMove(String gameID, ChessGame chessGame, String source, String target, Model model) {
        try {
            chessGame.move(new Square(source), new Square(target));
            chessService.movePiece(gameID, source, target);
            chessService.updateTurn(gameID, chessGame);
            promoteIfAvailable(chessGame, gameID, target);
            model.addAttribute("msg", "누가 이기나 보자구~!");
        } catch (IllegalArgumentException e) {
            model.addAttribute("msg", e.getMessage());
        }
    }

    private void promoteIfAvailable(ChessGame chessGame, String gameID, String target) {
        Square targetSquare = new Square(target);
        if(chessGame.isPromotionAvailable(targetSquare)){
            chessGame.doPromotion(targetSquare);
            chessService.promotePawnToQueen(gameID, target);
        }
    }

    private String getPosition(String input) {
        return input.split("=")[1];
    }

    @GetMapping("/restart")
    public String restartGame(@RequestParam String gameCode, Model model) {
        final String gameID = chessService.findGameID(gameCode);
        chessService.restartGame(gameID);
        ChessGame chessGame = chessService.loadSavedChessGame(gameID, chessService.getTurn(gameID));
        chessService.loadPieces(gameID);
        model.addAllAttributes(chessGame.getEmojis());
        GameResult gameResult = chessService.getGameResult(gameID);
        model.addAttribute("whiteScore", gameResult.calculateScore(Color.WHITE));
        model.addAttribute("blackScore", gameResult.calculateScore(Color.BLACK));
        model.addAttribute("turn", chessService.getTurn(gameID));
        model.addAttribute("msg", "누가 이기나 보자구~!");
        model.addAttribute("gameCode", gameCode);
        return "redirect:/ingame?gameCode=" + gameCode;
    }
}
