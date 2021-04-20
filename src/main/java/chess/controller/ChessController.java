package chess.controller;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chess.domain.chess.ChessDto;
import chess.domain.position.MovePosition;
import chess.service.ChessService;

@RestController
@RequestMapping("/api/chess")
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/{chessId}")
    public ResponseEntity<ChessDto> chessInfo(@PathVariable long chessId) {
        ChessDto chessDto = chessService.getChessGame(chessId);
        return ResponseEntity.ok(chessDto);
    }

    @PostMapping("")
    public ResponseEntity<Long> newChessGame() {
        Long chessId = chessService.insert();
        ResponseCookie cookie = ResponseCookie.from("chessId", String.valueOf(chessId))
                                              .path("/")
                                              .build();

        URI location = URI.create("/chess");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.created(location)
                             .headers(httpHeaders)
                             .build();
    }

    @PatchMapping("/{chessId}")
    public ResponseEntity<Void> move(@PathVariable long chessId, MovePosition movePosition) {
        chessService.move(chessId, movePosition);
        return ResponseEntity.noContent()
                             .build();
    }
}
