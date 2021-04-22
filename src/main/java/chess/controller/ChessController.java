package chess.controller;

import chess.dto.ChessRoomDto;
import chess.service.ChessGameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ChessController {

    private final ChessGameService chessGameService;

    public ChessController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public String index(@RequestParam long roomId, Model model) {
        model.addAttribute("roomId", roomId);
        return "index";
    }

    @GetMapping("/main")
    public String main(Model model) {
        List<ChessRoomDto> chessRooms = chessGameService.findChessRooms();
        model.addAttribute("chessRooms", chessRooms);
        return "main";
    }

}
