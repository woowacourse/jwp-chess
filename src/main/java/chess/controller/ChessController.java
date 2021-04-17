package chess.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import chess.domain.chess.ChessDTO;
import chess.domain.position.MovePosition;
import chess.service.ChessService;

@Controller
@RequestMapping("/chess")
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/view")
    public String chessTemplate() {
        return "/chess.html";
    }

    @GetMapping("/{chessId}")
    @ResponseBody
    public ResponseEntity<ChessDTO> chessInfo(@PathVariable long chessId) {
        ChessDTO chessDTO = chessService.getChessGame(chessId);
        return new ResponseEntity<>(chessDTO, HttpStatus.OK);
    }

    @PostMapping("")
    @ResponseBody
    public ResponseEntity<Long> newChessGame() {
        Long chessId = chessService.insert();
        ResponseCookie cookie = ResponseCookie.from("chessId", String.valueOf(chessId))
                                              .path("/")
                                              .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
        return new ResponseEntity<>(chessId, headers, HttpStatus.CREATED);
    }

    @PatchMapping("/{chessId}")
    @ResponseBody
    public HttpStatus move(@PathVariable long chessId, MovePosition movePosition) {
        chessService.move(chessId, movePosition);
        return HttpStatus.NO_CONTENT;
    }
}
