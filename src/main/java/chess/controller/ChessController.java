package chess.controller;

import chess.dto.ChessRoomDto;
import chess.service.ChessGameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ChessController {

    private final ChessGameService chessGameService;

    public ChessController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @PostMapping("/room/{roomId}")
    public String room(@PathVariable long roomId, Model model) {
        model.addAttribute("roomId", roomId);
        return "room";
    }

    @GetMapping("/")
    public String index(Model model) {
        List<ChessRoomDto> chessRooms = chessGameService.findChessRooms();
        model.addAttribute("chessRooms", chessRooms);
        return "index";
    }

}
