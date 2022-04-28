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
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    private final ChessGameService chessGameService;

    public WebController(final ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping(path = "/")
    public String waitingRoom(final Model model) {
        model.addAttribute("rooms", chessGameService.getRooms());
        return "waitingroom";
    }

    @PostMapping(path = "/")
    public String createWaitingRoom(@RequestParam("roomname") String name,
                                    @RequestParam("password") String password,
                                    final Model model) {
        model.addAttribute("rooms", chessGameService.createRoom(name, password));
        return "waitingroom";
    }

    @GetMapping(path = "/{roomNumber}/board")
    public String gameRoom(@PathVariable("roomNumber") int roomNumber, final Model model) {
        final Map<String, Object> pieces = convertToWebViewPiece(chessGameService.getPieces(roomNumber));
        model.addAttribute("id", roomNumber);
        model.addAllAttributes(pieces);
        return "index";
    }

    @GetMapping(path = "/{roomNumber}/start")
    public String start(@PathVariable("roomNumber") int roomNumber, final Model model) {
        final Map<String, Object> pieces = convertToWebViewPiece(chessGameService.start(roomNumber));
        model.addAttribute("id", roomNumber);
        model.addAllAttributes(pieces);
        return "index";
    }

    @PostMapping(path = "/{roomNumber}/move")
    public String move(final Model model,
                       @PathVariable("roomNumber") int roomNumber,
                       @RequestParam("source") String source,
                       @RequestParam("target") String target) {
        final Map<Position, Piece> pieces = chessGameService.move(roomNumber, source, target);
        model.addAttribute("id", roomNumber);
        model.addAllAttributes(convertToWebViewPiece(pieces));
        return "index";
    }

    @PostMapping(path = "/{roomNumber}/delete")
    public String deleteRoom(final Model model,
                       @PathVariable("roomNumber") int roomNumber,
                       @RequestParam("password") String password) {
        chessGameService.deleteRoom(roomNumber, password);
        return "redirect:/";
    }

    @GetMapping(path="/{roomNumber}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScoreDto> status(@PathVariable("roomNumber") int roomNumber) {
        final ScoreDto score = chessGameService.getScore(roomNumber);
        return ResponseEntity.ok(score);
    }

    @GetMapping(path = "/{roomNumber}/end")
    public String end(@PathVariable("roomNumber") int roomNumber, final Model model) {
        final Map<String, Object> pieces = convertToWebViewPiece(chessGameService.end(roomNumber));
        model.addAttribute("id", roomNumber);
        model.addAllAttributes(pieces);
        return "index";
    }
}
