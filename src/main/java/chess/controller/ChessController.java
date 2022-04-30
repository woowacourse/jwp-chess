package chess.controller;

import chess.dto.LogInDto;
import chess.service.ChessGameService;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessController {
    private static final String LOCALHOST_8080 = "http://localhost:8080";

    private final ChessGameService chessGameService;

    public ChessController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @PostMapping("/init")
    public ResponseEntity<Void> initGame(@ModelAttribute LogInDto logInDto) {
        chessGameService.createGame(logInDto);
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create(LOCALHOST_8080 + "/game/" + logInDto.getId()))
                .build();
    }

    @PostMapping("/enter")
    public ResponseEntity<Void> enterGame(@ModelAttribute LogInDto logInDto) {
        chessGameService.validateLogIn(logInDto);
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create(LOCALHOST_8080 + "/game/" + logInDto.getId()))
                .build();
    }

    @PostMapping("/exit")
    public ResponseEntity<Void> deleteGame(@ModelAttribute LogInDto logInDto) {
        chessGameService.validateEnd(logInDto.getId());
        chessGameService.cleanGame(logInDto);
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create(LOCALHOST_8080))
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> exception(Exception e) {
        String gameMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create(LOCALHOST_8080 + "?gameMessage=" + gameMessage))
                .build();
    }
}
