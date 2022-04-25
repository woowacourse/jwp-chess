package chess.controller;

import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.service.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.ui.Model;
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
        attributes.put("chessGames", service.loadAllChessGames());
        model.addAllAttributes(attributes);
        return "index";
    }

    @PostMapping("/create-chess-game")
    public RedirectView createChessGame(final @RequestParam String name) {
        service.createChessGame(name);
        return new RedirectView("/game/" + name);
    }

    @GetMapping("/game/{name}")
    public String getChessGamePage(final Model model, final @PathVariable String name) {
        Map<Position, Piece> currentBoard = service.loadChessGame(name).getCurrentBoard();
        Map<String, Piece> boardForModel = convertBoardForModel(currentBoard);
        model.addAllAttributes(boardForModel);
        model.addAttribute("chessGameName", name);
        model.addAttribute("turn", service.loadChessGame(name).getTurn());
        model.addAttribute("result", service.loadChessGame(name).generateResult());
        return "chess-game";
    }

    private Map<String, Piece> convertBoardForModel(final Map<Position, Piece> currentBoard) {
        return currentBoard.entrySet().stream()
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

    @PostMapping("/move/{chessGameName}")
    public RedirectView movePiece(final @PathVariable String chessGameName,
                                  final @RequestParam String source,
                                  final @RequestParam String target) {
        char[] sourcesElements = refineInputPosition(source);
        char[] targetElements = refineInputPosition(target);
        service.movePiece(
                chessGameName,
                sourcesElements[COLUMN_INDEX],
                Character.getNumericValue(sourcesElements[ROW_INDEX]),
                targetElements[COLUMN_INDEX],
                Character.getNumericValue(targetElements[ROW_INDEX])
        );
        return new RedirectView("/game/" + chessGameName);
    }

    private char[] refineInputPosition(final String position) {
        String refinedSource = position.trim().toLowerCase();
        if (refinedSource.length() != 2) {
            throw new IllegalArgumentException("[ERROR] 좌표를 잘못 입력했습니다.");
        }
        return refinedSource.toCharArray();
    }

    @PostMapping("/reset/{chessGameName}")
    public RedirectView resetChessGame(final @PathVariable String chessGameName) {
        service.createChessGame(chessGameName);
        return new RedirectView("/game/" + chessGameName);
    }
}
