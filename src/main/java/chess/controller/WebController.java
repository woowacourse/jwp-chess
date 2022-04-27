package chess.controller;

import chess.dto.BoardDto;
import chess.dto.GameDto;
import chess.dto.MoveDto;
import chess.dto.StatusDto;
import chess.service.ChessService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebController {

    private final ChessService chessService;

    @Autowired
    public WebController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public ModelAndView selectGame() {
        BoardDto boardDto = chessService.selectBoard();
        String winner = chessService.selectWinner();

        Map<String, Object> model = new HashMap<>();
        model.put("board", boardDto);
        model.put("winner", winner);

        return new ModelAndView("index", model);
    }

    @PostMapping("/game")
    @ResponseBody
    public ResponseEntity insertGame() {
        final Long gameId = chessService.insertGame();
        return ResponseEntity.ok().body(new GameDto(gameId));
    }

    @PutMapping("/game/board")
    @ResponseBody
    public ResponseEntity updateBoard(@RequestBody MoveDto moveDto) {
        final Long gameId = chessService.updateBoard(moveDto.getFrom(), moveDto.getTo());
        return ResponseEntity.ok().body(new GameDto(gameId));
    }

    @GetMapping("/game/status")
    @ResponseBody
    public ResponseEntity<StatusDto> selectStatus() {
        StatusDto statusDto = chessService.selectStatus();
        return ResponseEntity.ok().body(statusDto);
    }

    @DeleteMapping("/game")
    @ResponseBody
    public ResponseEntity deleteGame() {
        final ChessService chessService = this.chessService;
        final Long gameId = chessService.deleteGame();
        return ResponseEntity.ok().body(new GameDto(gameId));
    }
}
