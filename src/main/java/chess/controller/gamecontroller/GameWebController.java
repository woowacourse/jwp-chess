package chess.controller.gamecontroller;

import static chess.view.webview.Converter.convertToWebViewPiece;

import chess.dto.MoveRequestDto;
import chess.service.ChessGameService;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class GameWebController {

    private static final String RE_DIRECT = "redirect:/{roomId}";

    private final ChessGameService chessGameService;

    public GameWebController(final ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping(path = "/{roomId}")
    public String index(final Model model, @PathVariable("roomId") int roomId) {
        final Map<String, Object> pieces = convertToWebViewPiece(chessGameService.getPieces(roomId));
        model.addAllAttributes(pieces);
        model.addAttribute(roomId);
        return "game";
    }

    @GetMapping(path = "/{roomId}/start")
    public String start(@PathVariable("roomId") int roomId) {
        chessGameService.start(roomId);
        return RE_DIRECT;
    }

    @PostMapping(path = "/{roomId}/move")
    public String move(@PathVariable int roomId, @RequestBody MoveRequestDto moveRequest) {
        System.out.println(moveRequest);
        final String sourcePosition = moveRequest.getSource();
        final String targetPosition = moveRequest.getTarget();
        chessGameService.move(roomId, sourcePosition, targetPosition);
        return RE_DIRECT;
    }

    @GetMapping(path = "/{roomId}/end")
    public String end(@PathVariable int roomId) {
        chessGameService.end(roomId);
        return RE_DIRECT;
    }
}
