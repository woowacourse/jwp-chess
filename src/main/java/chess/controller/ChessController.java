package chess.controller;

import chess.domain.board.Position;
import chess.domain.game.ChessGame;
import chess.domain.game.GameRoom;
import chess.domain.piece.Piece;
import chess.service.ChessService;
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
public class ChessController {

    private static final int COLUMN_INDEX = 0;
    private static final int ROW_INDEX = 1;

    private final ChessService chessService;

    public ChessController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String getIndexPage(final Model model) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("gameRooms", chessService.loadAllGameRooms());
        model.addAllAttributes(attributes);
        return "index";
    }

    @PostMapping("/create-game-room")
    public RedirectView createGameRoom(final @RequestParam String name, final @RequestParam String password) {
        validateInputString(name);
        validateInputString(password);
        String gameRoomId = String.valueOf(name.hashCode());
        chessService.createGameRoom(gameRoomId, name, password);
        return new RedirectView("/game/" + gameRoomId);
    }

    private void validateInputString(final String input) {
        if (input.replace(" ", "").length() != input.length()) {
            throw new IllegalArgumentException("[ERROR] 입력값에 공백이 포함될 수 없습니다");
        }
        if (input.trim().isBlank()) {
            throw new IllegalArgumentException("[ERROR] 입력값이 비어있습니다.");
        }
    }

    @GetMapping("/game/{gameRoomId}")
    public String getChessGamePage(final Model model, final @PathVariable String gameRoomId) {
        GameRoom gameRoom = chessService.loadGameRoom(gameRoomId);
        ChessGame chessGame = gameRoom.getChessGame();
        model.addAllAttributes(convertBoardForModel(chessGame.getCurrentBoard()));
        model.addAttribute("gameRoomId", gameRoom.getGameRoomId());
        model.addAttribute("name", gameRoom.getName());
        model.addAttribute("turn", chessGame.getTurn());
        model.addAttribute("result", chessGame.generateResult());
        if (!gameRoom.getChessGame().isOn()) {
            return "game-result";
        }
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

    @PostMapping("/end/{gameRoomId}")
    public RedirectView endChessGame(final @PathVariable String gameRoomId) {
        chessService.endChessGame(gameRoomId);
        return new RedirectView("/game/" + gameRoomId);
    }

    @PostMapping("/delete/{gameRoomId}")
    public RedirectView deleteGameRoom(final @PathVariable String gameRoomId, final @RequestParam String password) {
        validateInputString(password);
        chessService.deleteGameRoom(gameRoomId, password);
        return new RedirectView("/");
    }

    @PostMapping("/move-piece/{gameRoomId}")
    public RedirectView movePiece(final @PathVariable String gameRoomId,
                                  final @RequestParam String source,
                                  final @RequestParam String target) {
        validateInputString(source);
        validateInputString(target);
        char[] sourcesElements = source.toLowerCase().toCharArray();
        char[] targetElements = target.toLowerCase().toCharArray();
        chessService.movePiece(
                gameRoomId,
                sourcesElements[COLUMN_INDEX],
                Character.getNumericValue(sourcesElements[ROW_INDEX]),
                targetElements[COLUMN_INDEX],
                Character.getNumericValue(targetElements[ROW_INDEX])
        );
        return new RedirectView("/game/" + gameRoomId);
    }

    @PostMapping("/reset/{gameRoomId}")
    public RedirectView resetChessGame(final @PathVariable String gameRoomId) {
        chessService.resetChessRoom(gameRoomId);
        return new RedirectView("/game/" + gameRoomId);
    }
}
