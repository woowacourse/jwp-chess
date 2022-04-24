package chess.controller;

import static chess.view.webview.Converter.convertToWebViewPiece;

import chess.domain.board.position.Position;
import chess.domain.piece.Piece;
import chess.dto.ScoreDto;
import chess.service.ChessGameService;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

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
    public String start(final Model model) {
        final Map<String, Object> pieces = convertToWebViewPiece(chessGameService.start());
        model.addAllAttributes(pieces);
        return "index";
    }

    @PostMapping(path = "/move")
    public String move(final Model model,
                     @RequestParam("source") String source,
                     @RequestParam("target") String target) {
        final Map<Position, Piece> pieces = chessGameService.move(source, target);
        model.addAllAttributes(convertToWebViewPiece(pieces));
        return "index";
    }

    @GetMapping(path="/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScoreDto> status() {
        final ScoreDto score = chessGameService.getScore();
        return ResponseEntity.ok(score);
    }

    @GetMapping(path = "/end")
    public String end(final Model model) {
        final Map<String, Object> pieces = convertToWebViewPiece(chessGameService.end());
        model.addAllAttributes(pieces);
        return "index";
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public String exception(Exception exception, Model model) {
        final Map<Position, Piece> pieces = chessGameService.getPieces();
        model.addAllAttributes(convertToWebViewPiece(pieces));
        model.addAttribute("error", exception.getMessage());
        return "index";
    }
}
