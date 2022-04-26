package chess.controller;

import chess.domain.game.ChessGame;
import chess.domain.piece.Piece;
import chess.service.ChessGameService;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class GameController {

    private static final int COLUMN_INDEX = 0;
    private static final int ROW_INDEX = 1;

    private final ChessGameService chessGameService;

    public GameController(final ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/games/{chessGameId}")
    public String getChessGamePage(final Model model, final @PathVariable long chessGameId) {
        ChessGame chessGame = chessGameService.loadChessGame(chessGameId);
        Map<String, Piece> boardForHtml = convertBoardForHtml(chessGame);
        model.addAllAttributes(boardForHtml);
        model.addAttribute("id",chessGameId);
        model.addAttribute("chess_game_name", chessGame.getName());
        model.addAttribute("turn", chessGame.getTurn());
        model.addAttribute("result", chessGame.generateResult());
        return "chess_game";
    }

    private Map<String, Piece> convertBoardForHtml(final ChessGame chessGame) {
        return chessGame.getCurrentBoard().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> String.valueOf(entry.getKey().getColumn().getValue()) +
                                entry.getKey().getRow().getValue(),
                        Entry::getValue
                ));
    }

    @PostMapping("/move/{chessGameId}")
    public RedirectView movePiece(final @PathVariable long chessGameId,
                                  final @RequestParam String source,
                                  final @RequestParam String target) {
        String refinedSource = source.trim().toLowerCase();
        String refinedTarget = target.trim().toLowerCase();
        chessGameService.movePiece(
                chessGameId,
                refinedSource.charAt(COLUMN_INDEX),
                Character.getNumericValue(refinedSource.charAt(ROW_INDEX)),
                refinedTarget.charAt(COLUMN_INDEX),
                Character.getNumericValue(refinedTarget.charAt(ROW_INDEX))
        );
        return new RedirectView("/games/" + chessGameId);
    }

    @PostMapping("/reset/{chessGameId}")
    public RedirectView resetChessGame(final @PathVariable long chessGameId) {
        chessGameService.resetChessGame(chessGameId);
        return new RedirectView("/games/" + chessGameId);
    }

}
