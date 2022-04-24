package chess.controller;

import chess.dto.BoardDto;
import chess.dto.MoveDto;
import chess.dto.StatusDto;
import chess.service.spring.ChessService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SpringWebChessController {

    private final ChessService chessService;

    @Autowired
    public SpringWebChessController(ChessService chessService) {
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

    @GetMapping("/game")
    public ModelAndView insertGame() {
        chessService.insertGame();
        return new ModelAndView("redirect:/");
    }

    @PutMapping("/game/board")
    public ModelAndView updateBoard(MoveDto moveDto) {
        chessService.updateBoard(moveDto.getFrom(), moveDto.getTo());
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/game/status")
    @ResponseBody
    public ResponseEntity<StatusDto> selectStatus() {
        StatusDto statusDto = chessService.selectStatus();
        return ResponseEntity.ok().body(statusDto);
    }

    @DeleteMapping("/game")
    public ModelAndView deleteGame() {
        chessService.deleteGame();
        return new ModelAndView("redirect:/");
    }
}
