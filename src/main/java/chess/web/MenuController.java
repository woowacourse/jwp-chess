package chess.web;

import chess.dto.BoardDto;
import chess.service.ChessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@RequestMapping("/rooms/{id}/board")
@Controller
public class MenuController {
    private final ChessService service;

    @GetMapping
    public String renderInitBoard(@PathVariable int id, Model model) {
        model.addAttribute("room-name", id);
        return "game";
    }

    @ResponseBody
    @GetMapping("/load")
    public ResponseEntity<BoardDto> loadBoard(@PathVariable int id) {
        return ResponseEntity.ok(new BoardDto(service.findBoardById(id)));
    }
}
