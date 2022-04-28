package chess.controller;

import chess.dto.MakeRoomDto;
import chess.service.ChessService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ChessGameViewController {

    private final ChessService chessService;

    public ChessGameViewController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String returnHomeView() {
        return "index";
    }

    @GetMapping("/create")
    public String returnRoomView() {
        return "room";
    }

    @GetMapping("/board/{id}")
    public String returnBoardView() {
        return "board";
    }

    @PostMapping("/room")
    public String createRoom(MakeRoomDto makeRoomDto) {
        Long id = chessService.makeGame(makeRoomDto);
        return "redirect:/board/" + id;
    }
}
