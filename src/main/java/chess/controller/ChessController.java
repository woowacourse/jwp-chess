package chess.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Long> newChessGame(HttpServletResponse response) {
        Long chessId = chessService.insert();

        Cookie chessIdCookie = new Cookie("chessId", String.valueOf(chessId));
        chessIdCookie.setMaxAge(60 * 60 * 24 * 30);
        response.addCookie(chessIdCookie);

        return new ResponseEntity<>(chessId, HttpStatus.CREATED);
    }

    @PatchMapping("/{chessId}")
    @ResponseBody
    public HttpStatus move(@PathVariable long chessId, MovePosition movePosition) {
        chessService.move(chessId, movePosition);
        return HttpStatus.NO_CONTENT;
    }
}
