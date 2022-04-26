package chess.controller;

import chess.dto.MoveDto;
import chess.service.ChessGameService;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PlayController {
    private static final String MOVE_MESSAGE = "이동했습니다";
    private final ChessGameService chessGameService;

    public PlayController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/game/start")
    public String startGame(@RequestParam String gameId) {
        chessGameService.createOrGet(gameId);
        return renderGame("", gameId);
    }

    @PostMapping(path = "/game/{gameId}/move")
    public String move(@PathVariable String gameId, @RequestBody MoveDto moveDto) {
        chessGameService.move(gameId, moveDto);
        return renderGame(MOVE_MESSAGE, gameId);
    }

    @GetMapping("/game/{gameId}/exit")
    public String exitAndDeleteGame(@PathVariable String gameId) {
        chessGameService.cleanGame(gameId);
        return "redirect:/";
    }

    @ExceptionHandler(Exception.class)
    public String exception(HttpServletRequest request, Exception e) {
        String gameId = request.getRequestURI().split("/")[2];
        return renderGame(e.getMessage(), gameId);
    }

    private String renderGame(String message, String gameId) {
        return "redirect:/game/" + gameId + "?message=" + URLEncoder.encode(message, StandardCharsets.UTF_8);
    }
}
