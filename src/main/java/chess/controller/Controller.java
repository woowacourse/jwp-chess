package chess.controller;

import chess.domain.piece.Piece;
import chess.service.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@org.springframework.stereotype.Controller
public class Controller {

    private static final int COLUMN_INDEX = 0;
    private static final int ROW_INDEX = 1;

    private final Service service;

    public Controller(final Service service) {
        this.service = service;
    }

    @GetMapping("/")
    public String getIndexPage(final Model model) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("chess_games", service.loadAllChessGames());
        model.addAllAttributes(attributes);
        return "index";
    }

    @PostMapping("/create_chess_game")
    public RedirectView createChessGame(final @RequestParam String name) {
        service.createChessGame(name);
        return new RedirectView("/game/" + name);
    }

    @GetMapping("/game/{name}")
    public String getChessGamePage(final Model model, final @PathVariable String name) {
        Map<String, Piece> boardForHtml = convertBoardForHtml(service, name);
        model.addAllAttributes(boardForHtml);
        model.addAttribute("chess_game_name", name);
        model.addAttribute("turn", service.loadChessGame(name).getTurn());
        model.addAttribute("result", service.loadChessGame(name).generateResult());
        return "chess_game";
    }

    private Map<String, Piece> convertBoardForHtml(final Service service, final String name) {
        return service.loadChessGame(name).getCurrentBoard().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> String.valueOf(entry.getKey().getColumn().getValue()) +
                                entry.getKey().getRow().getValue(),
                        Entry::getValue
                ));
    }

    @PostMapping("/delete/{name}")
    public RedirectView deleteChessGame(final @PathVariable String name) {
        service.deleteChessGame(name);
        return new RedirectView("/");
    }

    @PostMapping("/move/{chess_game_name}")
    public RedirectView movePiece(final @PathVariable String chess_game_name,
                                  final @RequestParam String source,
                                  final @RequestParam String target) {
        String refinedSource = source.trim().toLowerCase();
        String refinedTarget = target.trim().toLowerCase();
        service.movePiece(
                chess_game_name,
                refinedSource.charAt(COLUMN_INDEX),
                Character.getNumericValue(refinedSource.charAt(ROW_INDEX)),
                refinedTarget.charAt(COLUMN_INDEX),
                Character.getNumericValue(refinedTarget.charAt(ROW_INDEX))
        );
        return new RedirectView("/game/" + chess_game_name);
    }

    @PostMapping("/reset/{chess_game_name}")
    public RedirectView resetChessGame(final @PathVariable String chess_game_name) {
        service.createChessGame(chess_game_name);
        return new RedirectView("/game/" + chess_game_name);
    }

    @ExceptionHandler
    public ResponseEntity<String> getException(final Exception exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
