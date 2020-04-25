package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import wooteco.chess.controller.dto.ChessWebIndexDto;
import wooteco.chess.service.ChessService;

@Controller
public class ChessController {

    private ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index(Model model) {
        ChessWebIndexDto chessWebIndexDto = ChessWebIndexDto.of(chessService.getRoomId());
        model.addAttribute("chessRoomId", chessWebIndexDto);
        return "index";
    }
}
