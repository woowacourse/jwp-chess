package chess.controller;

import static chess.view.webview.Converter.convertToWebViewPiece;

import chess.service.ChessGameService;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    private static final String RE_DIRECT = "redirect:/";

    private final ChessGameService chessGameService;

    public WebController(final ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping(path = "/")
    public String index(final Model model) {
        final Map<String, Object> pieces = convertToWebViewPiece(chessGameService.getPieces());
        model.addAllAttributes(pieces);
        return "index";
    }

    @GetMapping(path = "/start")
    public String start() {
        chessGameService.start();
        return RE_DIRECT;
    }

    @PostMapping(path = "/move")
    public String move(@RequestParam("source") String source, @RequestParam("target") String target) {
        chessGameService.move(source, target);
        return RE_DIRECT;
    }

    @GetMapping(path = "/end")
    public String end() {
        chessGameService.end();
        return RE_DIRECT;
    }
}
