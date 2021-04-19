package chess.web.controller;

import chess.domain.position.MovePosition;
import chess.web.dto.ChessDto;
import chess.web.service.ChessService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/chess")
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/{chessId}")
    @ResponseBody
    public ResponseEntity<ChessDto> chessInfo(@PathVariable long chessId) {
        ChessDto chessDto = chessService.getChessGame(chessId);
        return ResponseEntity.ok(chessDto);
    }

    @GetMapping("/{chessId}/view")
    public String chessTemplate(@PathVariable long chessId) {
        return "/chess.html";
    }

    @PostMapping
    @ResponseBody
    public Object newChessGame(HttpServletResponse response) {
        Long chessId = chessService.insert();

        Cookie chessIdCookie = new Cookie("chessId", String.valueOf(chessId));
        chessIdCookie.setMaxAge(60 * 60 * 24 * 30);
        response.addCookie(chessIdCookie);

        return chessId;
    }

    @PatchMapping("/{chessId}")
    @ResponseBody
    public String move(@PathVariable long chessId, MovePosition movePosition) {
        chessService.move(chessId, movePosition);
        return "OK";
    }
}
